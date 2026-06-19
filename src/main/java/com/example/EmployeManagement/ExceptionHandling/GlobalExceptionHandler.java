package com.example.EmployeManagement.ExceptionHandling;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.EmployeeHome.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(ExceptionHandler.class);

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

        logger.error("Unhandled RuntimeException: {}", ex.getMessage(), ex);

        return new ApiResponse<>(
                "ERROR",
                ex.getMessage(),
                null,
                null
        );
    }


}
