package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.ExceptionHandling.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;

import org.springframework.security.core.
        authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.
        SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.
        OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter
        extends OncePerRequestFilter {

    private final AuthenticationService service;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null) {

            try {

                service.validateToken(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                token,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch (InvalidTokenException ex) {

                ApiResponse<Object> apiResponse = new ApiResponse<>(
                        "FAILURE",
                        ex.getMessage(),
                        null,
                        null
                );

                ObjectMapper objectMapper = new ObjectMapper();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                response.getWriter().write(
                        objectMapper.writeValueAsString(apiResponse)
                );

                return;
            }
        }


        filterChain.doFilter(request, response);
    }
}