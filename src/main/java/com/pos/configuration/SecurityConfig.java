package com.pos.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Security Configuration for the POS application
 * This class configures Spring Security to protect your APIs
 */
@Configuration
public class SecurityConfig {

    /**
     * Main security configuration method
     * This defines how your application handles authentication and authorization
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Session Management: STATELESS means no session cookies
                // Each request must contain authentication info (JWT token)
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Authorization Rules: Define which endpoints require authentication
                .authorizeHttpRequests(authorize -> authorize
                        // All /api/** endpoints require authentication (JWT token)
                        .requestMatchers("/api/**").authenticated()

                        // /api/super-admin/** requires ADMIN role
                        .requestMatchers("/api/super-admin/**").hasRole("ADMIN")

                        // All other endpoints are public (no authentication needed)
                        .anyRequest().permitAll())

                // Add JWT validation filter before Spring's basic auth filter
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class)

                // Disable CSRF (Cross-Site Request Forgery) protection
                // Safe to disable when using JWT tokens instead of cookies
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS (Cross-Origin Resource Sharing)
                // Allows frontend apps from different domains to call your API
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS Configuration
     * This allows your frontend application to call your backend API
     * even if they're running on different domains/ports
     */
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                // Allow requests from these origins (add your frontend URLs)
                cfg.setAllowedOrigins(Arrays.asList(
                        "http://localhost:3000",      // React default
                        "http://localhost:5173",      // Vite default
                        "http://localhost:4200"       // Angular default
                ));

                // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
                cfg.setAllowedMethods(Collections.singletonList("*"));

                // Allow cookies and authorization headers
                cfg.setAllowCredentials(true);

                // Allow all headers in requests
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                // Expose these headers to the frontend
                cfg.setExposedHeaders(List.of("Authorization"));

                // Cache CORS configuration for 1 hour
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }
}