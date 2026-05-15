package com.mourosub.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // llave secreta para firmar los tokens
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // metodo para crear el token usando el email
    public String generarToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    //getters y setters

    public Key getKey() {
        return key;
    }

}