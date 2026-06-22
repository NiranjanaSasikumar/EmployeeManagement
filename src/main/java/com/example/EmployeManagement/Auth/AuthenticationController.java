package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.User.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/generate")
    public AuthenticationEntity generateToken() {
        return service.generateToken();
    }

    @PostMapping("/signup")
    public ApiResponse<String> signup(
            @Valid @RequestBody SignupRequest request) {

        return service.signup(request);
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationEntity> login(
            @RequestBody SignupRequest request) {

        return service.login(request);
    }
}