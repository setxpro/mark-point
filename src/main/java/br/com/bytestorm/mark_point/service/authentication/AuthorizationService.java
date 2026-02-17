package br.com.bytestorm.mark_point.service.authentication;

import br.com.bytestorm.mark_point.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado para o email: " + username);
        }

        return user;
    }
}
