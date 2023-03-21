package com.pragma.shop.security.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtHandler {

    private final Environment environment;

    public DecodedJWT getDecodedJWT(String token) {
        Algorithm algorithm = getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public Algorithm getAlgorithm() {
        String secret = environment.getProperty("shop.secret");
        assert secret != null;
        return Algorithm.HMAC256(secret.getBytes());
    }
}
