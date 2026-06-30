package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.User.SignupRequest;
import com.example.EmployeManagement.User.User;
import com.example.EmployeManagement.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiry.minutes}")
    private long expiryMinutes;

    public ApiResponse<String> signup(
            SignupRequest request) {

        log.info(
                "Signup request received for username: {}",
                request.getUsername());

        log.info(
                "Checking if username already exists: {}",
                request.getUsername());

        if(userRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            log.error(
                    "Signup failed. Username already exists: {}",
                    request.getUsername());

            throw new RuntimeException(
                    "Username already exists");

        }

        if(request.getRole() == null ||
                request.getRole().isBlank()) {

            throw new RuntimeException(
                    "Role is required");
        }

        String role = request.getRole().toUpperCase();

        if(!role.equals("ADMIN")
                && !role.equals("MANAGER")
                && !role.equals("USER")) {

            throw new RuntimeException(
                    "Invalid role");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userRepository.save(user);

        log.info(
                "User registered successfully: {}",
                request.getUsername());

        return new ApiResponse<>(
                "SUCCESS",
                "User registered successfully",
                "Signup successful",
                null
        );
    }

    public ApiResponse<Object> login(
            SignupRequest request){

        log.info(
                "Login request received for username: {}",
                request.getUsername());

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> {

                    log.error(
                            "Login failed. Username not found: {}",
                            request.getUsername());

                    return new RuntimeException(
                            "Invalid username or password");
                });

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            log.error(
                    "Login failed. Invalid password for username: {}",
                    request.getUsername());

            throw new RuntimeException(
                    "Invalid username or password");
        }

        log.info(
                "Login successful for username: {}",
                request.getUsername());

        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole());

        Map<String, Object> response = new HashMap<>();

        LocalDateTime expiryTime =
                LocalDateTime.now().plusMinutes(expiryMinutes);

        response.put("token", token);
        response.put("expiresAt", expiryTime);

        return new ApiResponse<>(
                "SUCCESS",
                "Login successful",
                response,
                null
        );
    }

}