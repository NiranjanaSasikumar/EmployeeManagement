package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.ExceptionHandling.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final AuthenticationRepository repository;

    public AuthenticationService(
            AuthenticationRepository repository) {

        this.repository = repository;
    }


    @Value("${token.expiry.minutes}")
    private long expiryMinutes;

    public AuthenticationEntity generateToken() {


        AuthenticationEntity auth =
                new AuthenticationEntity();

        String token = UUID.randomUUID().toString();

        auth.setToken(token);

        auth.setExpiryTime(
                LocalDateTime.now()
                        .plusMinutes(expiryMinutes));

        AuthenticationEntity saved = repository.save(auth);

        return saved;
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