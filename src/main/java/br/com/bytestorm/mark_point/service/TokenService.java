package br.com.bytestorm.mark_point.service;

import br.com.bytestorm.mark_point.entity.User;
import com.auth0.jwt.interfaces.DecodedJWT;

public interface TokenService {
    String generateToken(User user);
    String validateTokenOld(String token);
    DecodedJWT validateToken(String token);
}
