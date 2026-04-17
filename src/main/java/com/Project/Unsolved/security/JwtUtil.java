package com.Project.Unsolved.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // 1️⃣ Generate token
    public String generateToken(Long userId, String role) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))     // who
                .claim("role", role)                     // authority
                .setIssuedAt(new Date())                 // when issued
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    // 2️⃣ Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 3️⃣ Extract userId
    public Long extractUserId(String token) {
        return Long.parseLong(
                Jwts.parser().setSigningKey(secret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }

    // 4️⃣ Extract role
    public String extractRole(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
