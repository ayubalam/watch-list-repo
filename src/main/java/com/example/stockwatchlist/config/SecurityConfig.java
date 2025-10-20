package com.example.stockwatchlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Define the PasswordEncoder Bean (BCrypt is standard practice)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configure the Security Filter Chain (Authorization Rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API testing (re-enable for production and Thymeleaf forms)
                .authorizeHttpRequests(auth -> auth
                        // Allow POST to register a new user without authentication
                        .requestMatchers("/api/users").permitAll()

                        // Allow access to static resources and the login/home pages
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/").permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll() // Custom login page (we'll create the Thymeleaf template)
                        .defaultSuccessUrl("/watchlist", true) // Redirect to watchlist on successful login
                )
                .logout(logout -> logout
                        .permitAll()
                );

        return http.build();
    }
}