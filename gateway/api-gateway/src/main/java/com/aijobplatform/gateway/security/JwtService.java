package com.aijobplatform.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key";

    private final Key SECRET_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {

        Claims claims = extractAllClaims(token);

        Date expiration = claims.getExpiration();

        if (expiration != null &&
                expiration.before(new Date())) {

            throw new RuntimeException("Token expired");
        }

        return true;
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}