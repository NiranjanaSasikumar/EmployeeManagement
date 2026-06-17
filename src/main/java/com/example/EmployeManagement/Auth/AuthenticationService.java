package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.ExceptionHandling.InvalidTokenException;
import com.example.EmployeManagement.User.SignupRequest;
import com.example.EmployeManagement.User.UserEntity;
import com.example.EmployeManagement.User.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public AuthenticationEntity signup(
            SignupRequest request) {

        if(userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        return generateToken();
    }

    @Value("${token.expiry.minutes}")
    private long expiryMinutes;

    public AuthenticationEntity generateToken() {

        String token = UUID.randomUUID().toString();


        AuthenticationEntity auth =
                AuthenticationEntity.builder()
                        .token(token)
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(expiryMinutes))
                        .build();

        return repository.save(auth);
    }

    public void validateToken(String token) {

        AuthenticationEntity auth =
                repository.findByToken(token)
                        .orElseThrow(() ->
                                new InvalidTokenException(
                                        "Invalid Token"));

        if(auth.getExpiryTime()
                .isBefore(LocalDateTime.now())) {

            throw new InvalidTokenException(
                    "Token Expired");
        }
    }
}