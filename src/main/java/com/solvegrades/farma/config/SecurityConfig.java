package com.solvegrades.farma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/api/v1/empleados/login", // permite login
                    "/api/v1/empleados",      // permite crear empleados (POST)
                    "/h2-console/**"          // si usas consola H2
                ).permitAll()
                .anyRequest().permitAll() // puedes cambiar a authenticated() cuando quieras proteger el resto
            )
            .formLogin(form -> form.disable()) // desactiva el login por formulario
            .httpBasic(httpBasic -> httpBasic.disable()); // desactiva auth básica
        return http.build();
    }
}