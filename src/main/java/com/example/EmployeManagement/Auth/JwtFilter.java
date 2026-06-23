package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.DTO.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final JwtUtil jwtUtil;

    private static final Logger logger =
            LoggerFactory.getLogger(JwtFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        logger.info("Incoming request: {}", request.getRequestURI());

        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            logger.info("Authorization header received");

            token = authHeader.substring(7);
        }

        if (token != null) {

            try {

                logger.info("Validating authentication token");

                if (!jwtUtil.validateToken(token)) {
                    throw new RuntimeException("Invalid Token");
                }

                logger.info("Token validated successfully");

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token).toUpperCase();
                logger.info("Role extracted from JWT: {}", role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority("ROLE_" + role)
                                )
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                logger.info("Authentication context set successfully");

            } catch (Exception ex) {

                logger.error(
                        "Token validation failed: {}",
                        ex.getMessage());

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

        logger.info("Request processing completed");
    }
}