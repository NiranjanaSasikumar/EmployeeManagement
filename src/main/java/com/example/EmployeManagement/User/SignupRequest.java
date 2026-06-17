package com.example.EmployeManagement.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "Username cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z ]+$",
            message = "Name must contain only letters"
    )
    @Size(min = 3, max = 30,
            message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min =6,
            message = "Password must contain at least 6 characters")
    private String password;
}
