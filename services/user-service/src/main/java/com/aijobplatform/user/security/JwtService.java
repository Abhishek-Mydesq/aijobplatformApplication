package com.aijobplatform.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET =
            "my-secret-key-my-secret-key-my-secret-key"; // must be >= 32 chars

    private final Key SECRET_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    private final long ACCESS_EXPIRATION =
            1000 * 60 * 60; // 1 hour

    private final long REFRESH_EXPIRATION =
            1000L * 60 * 60 * 24 * 7; // 7 days


    // ===== ACCESS TOKEN =====

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + ACCESS_EXPIRATION
                        )
                )
                .signWith(SECRET_KEY)
                .compact();
    }


    // ===== REFRESH TOKEN =====

    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + REFRESH_EXPIRATION
                        )
                )
                .signWith(SECRET_KEY)
                .compact();
    }


    // ===== EXTRACT EMAIL =====

    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }


    // ===== VALIDATE =====

    public boolean validateToken(String token) {

        extractAllClaims(token);
        return true;
    }


    // ===== PARSE =====

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}