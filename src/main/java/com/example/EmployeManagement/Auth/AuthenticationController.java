package com.example.EmployeManagement.Auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(
            AuthenticationService service) {

        this.service = service;
    }

    @PostMapping("/generate")
    public AuthenticationEntity generateToken() {
        return service.generateToken();
    }
}