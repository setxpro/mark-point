package br.com.bytestorm.mark_point.service.impl;

import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.entity.dto.ListUsersDTO;
import br.com.bytestorm.mark_point.entity.dto.UserDTO;
import br.com.bytestorm.mark_point.entity.mapper.UserMapper;
import br.com.bytestorm.mark_point.enums.EnumException;
import br.com.bytestorm.mark_point.exception.NegocioException;
import br.com.bytestorm.mark_point.exception.ServiceException;
import br.com.bytestorm.mark_point.repository.UserRepository;
import br.com.bytestorm.mark_point.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final SecureRandom RNG = new SecureRandom();

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT = "0123456789";
    private static final String SYMBOL = "!@#$%^&*()-_=+[]{};:,.?";

    private static final String ALL = LOWER + UPPER + DIGIT + SYMBOL;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public void create(UserDTO userDTO) {
        User oUser = userMapper.toEntityUser(userDTO);
        validateAndSaveUser(oUser);

        // Send Notification with password.
        String from = "";
        String to = "";
        String subject = "";
        String name = "";
        String message = "";


    }

    @Override
    public void update(UUID oid, UserDTO userDTO) {

        User user = userRepository.findById(oid).orElseThrow(
                () -> new ServiceException("Usuário não encontrado.", EnumException.NOT_FOUND_404)
        );

        validateAndUpdateUser(userDTO, user);
    }

    @Transactional
    private void validateAndUpdateUser(UserDTO userDTO, User user) {
        if (
                userDTO.name() == null  || userDTO.name().isEmpty() ||
                        userDTO.email() == null || userDTO.email().isEmpty() ||
                        userDTO.phone() == null || userDTO.phone().isEmpty()
        ) throw new NegocioException(
                "Por favor, preencha todos os campos.",
                EnumException.BAD_REQUEST_400
        );

        isNomeProprioValido(userDTO.name());
        isEmailUpdateValid(userDTO.email(), user.getId());
        isPhoneValidToUpdate(userDTO.phone(), user.getId());

        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPhone(userDTO.phone());
        user.setGoogleId(userDTO.googleId());
        user.setPushNotificationId(userDTO.pushNotificationId());

        this.userRepository.save(user);
    }

    @Transactional
    private void validateAndSaveUser(User oUser) {

            // Validate input
            if (oUser.getName() == null || oUser.getName().isEmpty()
                    || oUser.getEmail() == null || oUser.getEmail().isEmpty()
                    || oUser.getPhone() == null || oUser.getPhone().isEmpty()) {

                throw new NegocioException(
                        "Por favor, preencha todos os campos.",
                        EnumException.BAD_REQUEST_400
                );
            }

            // Validate request fields
            isNomeProprioValido(oUser.getName());
            isEmailValido(oUser.getEmail());
            isTelefoneValido(oUser.getPhone());

            String createPassword = createStrongPassword();
            String afterEncoder = passwordEncoder.encode(createPassword);
            oUser.setPassword(afterEncoder);

            log.info("PASSWORD SAVED: {}", createPassword);
            this.userRepository.save(oUser);
    }

    @Override
    public List<ListUsersDTO> list() {
        return userRepository.findAll().stream().map(userMapper::toListDto).toList();
    }

    @Override
    public ListUsersDTO findOne(UUID uid) {
        return userRepository.findById(uid).map(userMapper::toListDto).orElseThrow(
                () -> new ServiceException("Usuário não encontrado.", EnumException.NOT_FOUND_404)
        );
    }

    @Override
    public void delete(UUID uid) {
        userRepository.findById(uid).orElseThrow(
                () -> new ServiceException("Usuário não encontrado.", EnumException.NOT_FOUND_404)
        );
        userRepository.deleteById(uid);
    }

    ///

    public String createStrongPassword() {
        int length = 14; // pode ajustar: 12-16 costuma ser ótimo

        // garante pelo menos 1 de cada categoria
        char[] pwd = new char[length];
        pwd[0] = LOWER.charAt(RNG.nextInt(LOWER.length()));
        pwd[1] = UPPER.charAt(RNG.nextInt(UPPER.length()));
        pwd[2] = DIGIT.charAt(RNG.nextInt(DIGIT.length()));
        pwd[3] = SYMBOL.charAt(RNG.nextInt(SYMBOL.length()));

        for (int i = 4; i < length; i++) {
            pwd[i] = ALL.charAt(RNG.nextInt(ALL.length()));
        }

        // embaralha (Fisher-Yates)
        for (int i = pwd.length - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            char tmp = pwd[i];
            pwd[i] = pwd[j];
            pwd[j] = tmp;
        }

        return new String(pwd);
    }

    public void validateStrongPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new NegocioException("Senha não pode ser vazia.");
        }

        // regras
        int minLen = 8;
        if (password.length() < minLen) {
            throw new NegocioException("Senha fraca: mínimo de " + minLen + " caracteres.");
        }
        if (password.contains(" ")) {
            throw new NegocioException("Senha inválida: não pode conter espaços.");
        }

        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSymbol = password.chars().anyMatch(ch -> SYMBOL.indexOf(ch) >= 0);

        if (!hasLower || !hasUpper || !hasDigit || !hasSymbol) {
            throw new NegocioException(
                    "Senha fraca: use ao menos 1 letra minúscula, 1 maiúscula, 1 número e 1 símbolo."
            );
        }

        // (opcional) evitar senhas comuns / muito simples
        String lower = password.toLowerCase();
        if (lower.contains("password") || lower.contains("123456") || lower.contains("qwerty")) {
            throw new NegocioException("Senha fraca: evite senhas comuns ou sequências óbvias.");
        }
    }

    private void isNomeProprioValido(String nome) {
        String regex = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(\\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)+$";
        boolean isValid = nome.trim().matches(regex);
        if (!isValid) {
            throw new NegocioException("Por favor, insira ao menos um nome válido. Ex.: Jhon Doe");
        }
    }

    private void isEmailValido(String email) {

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValid = email.matches(regex);

        if (!isValid) {
            throw new NegocioException("Por favor, insira ao menos um e-mail válido. Ex.: exemplo@gmail.com");
        }

        var userExists = userRepository.findByEmail(email);

        if (userExists != null) {
            throw new ServiceException("E-mail já registrado na base de dados.", EnumException.CONFLICT_409);
        }
    }

    private void isEmailUpdateValid(String email, UUID id) {

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValid = email.matches(regex);

        if (!isValid) {
            throw new NegocioException("Por favor, insira ao menos um e-mail válido. Ex.: exemplo@gmail.com");
        }

        boolean userExists = userRepository.existsByEmailAndIdNot(email, id);

        if (userExists) {
            throw new ServiceException("E-mail já registrado na base de dados.", EnumException.CONFLICT_409);
        }
    }

    public void isTelefoneValido(String telefone) {
        String regex = "^\\d{10,11}$";
        boolean isValid = telefone.matches(regex);

        if (!isValid) {
            throw new NegocioException("Por favor, insira ao menos um telefone válido.");
        }

        Long phoneExists = userRepository.userPhoneExists(telefone);

        if (phoneExists != 0) {
            throw new ServiceException("Telefone já registrado na base de dados.", EnumException.CONFLICT_409);
        }
    }

    public void isPhoneValidToUpdate(String telefone, UUID id) {
        String regex = "^\\d{10,11}$";
        boolean isValid = telefone.matches(regex);

        if (!isValid) {
            throw new NegocioException("Por favor, insira ao menos um telefone válido.");
        }

        boolean phoneExists = userRepository.existsByPhoneAndIdNot(telefone, id);

        if (phoneExists) {
            throw new ServiceException("Telefone já registrado na base de dados.", EnumException.CONFLICT_409);
        }
    }
}
