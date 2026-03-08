package com.aluracursos.forohub.infra.security;

import com.aluracursos.forohub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generarToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withIssuer("forohub")
                .withSubject(usuario.getCorreoElectronico())
                .withExpiresAt(Instant.now().plus(expiration, ChronoUnit.HOURS))
                .sign(algorithm);
    }

    public String getSubject(String tokenJWT) {
        if (tokenJWT == null) {
            return null;
        }

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer("forohub")
                .build()
                .verify(tokenJWT)
                .getSubject();
    }
}