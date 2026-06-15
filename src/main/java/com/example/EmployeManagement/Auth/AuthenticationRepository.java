package com.example.EmployeManagement.Auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository
        extends JpaRepository<AuthenticationEntity, String> {

    Optional<AuthenticationEntity> findByToken(String token);
}