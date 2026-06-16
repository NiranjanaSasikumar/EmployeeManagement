package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.User.SignupRequest;
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

    @PostMapping("/signup")
    public AuthenticationEntity signup(
            @RequestBody SignupRequest request) {

        return service.signup(request);
    }
}