package com.example.EmployeManagement.Auth;

import com.example.EmployeManagement.ExceptionHandling.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/employee/*/increment")
                        .hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/employee/**",
                                "/department/**")
                        .hasAnyRole("USER", "MANAGER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/employee/**",
                                "/department/**")
                        .hasAnyRole("MANAGER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/employee/**",
                                "/department/**")
                        .hasAnyRole("MANAGER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/employee/**",
                                "/department/**")
                        .hasAnyRole("MANAGER", "ADMIN")

                        .anyRequest()
                        .authenticated())

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
