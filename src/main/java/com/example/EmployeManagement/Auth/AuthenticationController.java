package com.example.EmployeManagement.Auth;

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
    public AuthenticationEntity signup(
            @Valid @RequestBody SignupRequest request) {

        return service.signup(request);
    }
}