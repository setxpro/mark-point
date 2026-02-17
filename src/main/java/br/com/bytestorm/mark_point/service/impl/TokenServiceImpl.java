package br.com.bytestorm.mark_point.service.impl;

import br.com.bytestorm.mark_point.entity.User;
import br.com.bytestorm.mark_point.enums.EnumException;
import br.com.bytestorm.mark_point.exception.ServiceException;
import br.com.bytestorm.mark_point.repository.UserRepository;
import br.com.bytestorm.mark_point.service.TokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String generateToken(User user) {
        try {

            // Get Secret Key Decoded
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Get User
            User userExists = userRepository.findById(user.getId()).orElseThrow(() -> new ServiceException("Usuário não encontrado com o Id: " + user.getId(), EnumException.NOT_FOUND_404));

            // Get Roles
            var roles = userExists.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray(String[]::new);

            if (roles.length == 0) {
                throw new ServiceException("Usuário não possui acesso aos recursos solicitados.", EnumException.NOT_FOUND_404);
            }

            // Get Token
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withArrayClaim("roles", roles)
                    .withClaim("name", user.getName())
                    .withClaim("email", user.getEmail())
                    .withClaim("phone", user.getPhone())
                    .withClaim("userId", String.valueOf(user.getId()))
                    .withClaim("googleId", user.getGoogleId())
                    .withClaim("pushNotificationId", user.getPushNotificationId())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new ServiceException("Error while generating token", EnumException.BAD_REQUEST_400);
        }
    }

    @Override
    public String validateTokenOld(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "INVALID TOKEN";
        }
    }

    @Override
    public DecodedJWT validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token);
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(48).toInstant(ZoneOffset.of("-03:00"));
    }
}
