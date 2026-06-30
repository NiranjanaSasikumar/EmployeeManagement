package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.User.SignupRequest;
import com.example.EmployeManagement.Util.ApiRoutes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.AUTH)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ApiResponse<String> signup(
            @Valid @RequestBody SignupRequest request) {

        return service.signup(request);
    }

    @PostMapping("/login")
    public ApiResponse<Object> login(
            @RequestBody SignupRequest request) {

        return service.login(request);
    }
}

