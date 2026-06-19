package com.example.EmployeManagement.Auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationEntity {

    @Id
    private String token;

    private LocalDateTime expiryTime;

}