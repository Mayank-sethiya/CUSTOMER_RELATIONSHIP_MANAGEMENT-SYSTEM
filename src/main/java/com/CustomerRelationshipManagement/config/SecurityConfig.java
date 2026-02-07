package com.CustomerRelationshipManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Enable CORS using the bean below
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API ease-of-use
                .authorizeHttpRequests(authz -> authz
                        // Permit all static frontend assets and login/index
                        .requestMatchers("/", "/index.html", "/config.js", "/admin/**", "/images/**", "/user/**", "/video/**").permitAll()
                        // Permit all backend API calls
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Updated origins to include your new Render live URL
        configuration.setAllowedOrigins(List.of(
                "http://localhost:53929",
                "http://localhost:63342",
                "https://customer-relationship-management-system-qrpu.onrender.com" // 👈 Added your Render link
        ));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
