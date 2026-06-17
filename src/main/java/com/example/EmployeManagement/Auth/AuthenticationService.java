package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.ExceptionHandling.InvalidTokenException;
import com.example.EmployeManagement.User.SignupRequest;
import com.example.EmployeManagement.User.User;
import com.example.EmployeManagement.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository repository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger =
            LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationEntity signup(
            SignupRequest request) {

        logger.info(
                "Signup request received for username: {}",
                request.getUsername());

        logger.info(
                "Checking if username already exists: {}",
                request.getUsername());

        if(userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            logger.error(
                    "Signup failed. Username already exists: {}",
                    request.getUsername());

            throw new RuntimeException(
                    "Username already exists");

        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        logger.info(
                "User registered successfully: {}",
                request.getUsername());

        return generateToken();
    }

    @Value("${token.expiry.minutes}")
    private long expiryMinutes;

    public AuthenticationEntity generateToken() {

        logger.info("Generating authentication token");

        String token = UUID.randomUUID().toString();


        AuthenticationEntity auth =
                AuthenticationEntity.builder()
                        .token(token)
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(expiryMinutes))
                        .build();

        logger.info("Token generated successfully.");

        return repository.save(auth);
    }

    public void validateToken(String token) {

        logger.info("Validating authentication token");

        AuthenticationEntity auth =
                repository.findByToken(token)
                        .orElseThrow(() ->
                                new InvalidTokenException(
                                        "Invalid Token"));

        if(auth.getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            logger.error(
                    "Token validation failed. Token expired");

            throw new InvalidTokenException(
                    "Token Expired");
        }

    }
}