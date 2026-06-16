package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.ExceptionHandling.InvalidTokenException;
import com.example.EmployeManagement.User.SignupRequest;
import com.example.EmployeManagement.User.UserEntity;
import com.example.EmployeManagement.User.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final AuthenticationRepository repository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationRepository repository,
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationEntity signup(
            SignupRequest request) {

        if(userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists");
        }

        UserEntity user = new UserEntity();

        user.setUsername(
                request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        userRepository.save(user);

        return generateToken();
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