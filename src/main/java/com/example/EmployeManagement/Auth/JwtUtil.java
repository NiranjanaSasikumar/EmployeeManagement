package com.example.EmployeManagement.Auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiry.minutes}")
    private long expiryMinutes;

    private static final Logger logger =
            LoggerFactory.getLogger(JwtUtil.class);

    public String generateToken(
            String username,
            String role) {

        logger.info(
                "Generating JWT token for user: {} with role: {}",
                username,
                role);

        SecretKey key = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));

        logger.info(
                "JWT token will expire in {} minutes",
                expiryMinutes);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
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

        logger.info("Extracting username from token");

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

        logger.info("Extracting role from token");

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

            logger.info("Validating JWT token");

            SecretKey key = Keys.hmacShaKeyFor(
                    secretKey.getBytes(StandardCharsets.UTF_8));

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            logger.info("JWT token validated successfully");

            return true;

        } catch (Exception e) {

            logger.error(
                    "JWT validation failed: {}",
                    e.getMessage()
            );

            return false;
        }
    }

}
