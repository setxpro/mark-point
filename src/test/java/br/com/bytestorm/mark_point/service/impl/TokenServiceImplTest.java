package br.com.bytestorm.mark_point.service.impl;

import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.enums.EnumException;
import br.com.bytestorm.mark_point.exception.ServiceException;
import br.com.bytestorm.mark_point.repository.UserRepository;
import br.com.bytestorm.mark_point.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Ref;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class TokenServiceImplTest {

    private TokenService tokenService;
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        tokenService = new TokenServiceImpl();

        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-123");
        ReflectionTestUtils.setField(tokenService, "userRepository", userRepository);
    }

    @Test
    void generateToken_shouldGenerateToken_withClaimsAndRoles() {
        // arrange
        User input = new User();
        input.setId(UUID.randomUUID());
        input.setEmail("patrick@teste.com");
        input.setName("Patrick");
        input.setPhone("21999999999");
        input.setGoogleId("google-sub-123");
        input.setPushNotificationId("push-1");
        input.setCreatedAt(Instant.now());
        input.setIsActive(true);

        input.setRole(br.com.bytestorm.mark_point.enums.RoleEnum.CUSTOMER);

        when(userRepository.findById(input.getId())).thenReturn(Optional.of(input));

        // act
        String token = tokenService.generateToken(input);

        // assert
        assertNotNull(token);
        assertFalse(token.isBlank());


        DecodedJWT jwt = tokenService.validateToken(token);
        assertEquals("auth-api", jwt.getIssuer());
        assertEquals("patrick@teste.com", jwt.getSubject());
        assertEquals("Patrick", jwt.getClaim("name").asString());
        assertEquals("patrick@teste.com", jwt.getClaim("email").asString());
        assertEquals("21999999999", jwt.getClaim("phone").asString());
        assertEquals(input.getId().toString(), jwt.getClaim("userId").asString());
        assertEquals("google-sub-123", jwt.getClaim("googleId").asString());
        assertEquals("push-1", jwt.getClaim("pushNotificationId").asString());

        String[] roles = jwt.getClaim("roles").asArray(String.class);
        assertNotNull(roles);
        assertTrue(roles.length >= 1);
    }

    @Test
    void generateToken_shouldThrow_whenUserNotFound() {
        // arrange
        User input = new User();
        input.setId(UUID.randomUUID());

        when(userRepository.findById(input.getId())).thenReturn(Optional.empty());

        // act + assert
        ServiceException ex = assertThrows(ServiceException.class, () -> tokenService.generateToken(input));
        assertEquals(EnumException.NOT_FOUND_404, ex.getEnumException());
    }

    @Test
    void generateToken_shouldThrow_whenUserHasNoRoles() {
        // arrange
        User input = new User();
        input.setId(UUID.randomUUID());
        input.setEmail("no-role@teste.com");
        input.setName("No Role");
        input.setCreatedAt(Instant.now());
        input.setIsActive(true);

        // role nulo => seu getAuthorities retorna ROLE_BASIC (ou pode dar comportamento diferente dependendo do enum)
        // então, pra forçar roles.length == 0, o jeito correto é: ajustar o código de produção
        // MAS, se você realmente quer testar esse cenário, recomendo trocar a regra no User.getAuthorities
        // para retornar List.of() quando role == null, ou lançar exceção.
        //
        // Aqui vou simular pelo caminho mais real: usar um spy e forçar getAuthorities vazio:
        User spyUser = Mockito.spy(input);
        doReturn(java.util.List.of()).when(spyUser).getAuthorities();

        when(userRepository.findById(spyUser.getId())).thenReturn(Optional.of(spyUser));

        // act + assert
        ServiceException ex = assertThrows(ServiceException.class, () -> tokenService.generateToken(spyUser));
        assertEquals(EnumException.NOT_FOUND_404, ex.getEnumException());
    }

    @Test
    void validateToken_shouldThrow_whenTokenIsInvalid() {
        // arrange
        String invalidToken = "abc.def.ghi";

        // act + assert
        assertThrows(JWTVerificationException.class, () -> tokenService.validateToken(invalidToken));
    }
}