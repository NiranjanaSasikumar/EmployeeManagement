package com.example.EmployeManagement.ExceptionHandling;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException {

        ApiResponse<Object> apiResponse =
                new ApiResponse<>(
                        "FAILURE",
                        "You do not have permission to access this resource",
                        null,
                        null
                );

        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        response.getWriter().write(
                objectMapper.writeValueAsString(apiResponse)
        );
    }

}
