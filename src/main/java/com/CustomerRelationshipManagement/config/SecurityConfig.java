package com.CustomerRelationshipManagement.config; // Make sure this package name is correct

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
                // 1. Enable CORS using the configuration below
                .cors(withDefaults())

                // 2. Disable CSRF for APIs
                .csrf(csrf -> csrf.disable())

                // 3. Set authorization rules
                // Inside your SecurityConfig.java, replace the authorizeHttpRequests section with this:
                .authorizeHttpRequests(authz -> authz
                        // 1. IMPORTANT: Allow public access to your landing page and all static assets
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/config.js",
                                "/admin/**",
                                "/images/**",
                                "/user/**",
                                "/video/**"
                        ).permitAll()

                        // 2. Allow public access to all of your backend API endpoints
                        .requestMatchers("/api/**").permitAll()

                        // 3. For any other request, require the user to be authenticated
                        .anyRequest().authenticated()
                );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 4. IMPORTANT: Add all addresses your frontend uses
        configuration.setAllowedOrigins(List.of(
                "http://localhost:53929",
                "http://localhost:63342",
                "https://trackforce-crm-01.azurewebsites.net", // Replace with your Azure URL
                "https://www.trackforcecrm.com" // Your future custom domain
                // Add any other ports if necessary
        ));

        // 5. Allow all necessary methods and headers
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}