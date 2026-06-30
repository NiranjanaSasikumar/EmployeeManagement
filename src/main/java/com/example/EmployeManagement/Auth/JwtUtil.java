package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.User.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiry.minutes}")
    private long expiryMinutes;

    public String generateToken(
            String username,
            Role role) {

        log.info(
                "Generating JWT token for user: {} with role: {}",
                username,
                role);

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));

        log.info(
                "JWT token will expire in {} minutes",
                expiryMinutes);

        return Jwts.builder()
                .subject(username)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(
                        Date.from(
                                Instant.now()
                                        .plus(expiryMinutes,
                                                ChronoUnit.MINUTES)
                        )
                )
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {

        log.info("Extracting username from token");

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {

        log.info("Extracting role from token");

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public boolean validateToken(String token) {

        try {

            log.info("Validating JWT token");

            SecretKey key = Keys.hmacShaKeyFor(
                    secretKey.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            log.info("JWT token validated successfully");

            return true;

        } catch (Exception e) {

            log.error(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            return false;
        }
    }

}
