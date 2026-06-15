package com.example.EmployeManagement.ExceptionHandling;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {

        super(message);
    }
}
