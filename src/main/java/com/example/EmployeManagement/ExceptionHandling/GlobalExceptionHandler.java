package com.example.EmployeManagement.ExceptionHandling;

import com.example.EmployeManagement.DTO.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationException(
            MethodArgumentNotValidException ex) {


        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        return new ApiResponse<>(
                "FAILURE",
                "Validation Failed",
                errors,
                null
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object>
    handleRuntimeException(
            RuntimeException ex) {

        log.error("Unhandled RuntimeException: {}", ex.getMessage(), ex);

        return new ApiResponse<>(
                "ERROR",
                ex.getMessage(),
                null,
                null
        );
    }


}
