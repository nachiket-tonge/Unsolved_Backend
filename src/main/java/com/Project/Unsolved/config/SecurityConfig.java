package com.Project.Unsolved.config;

import com.Project.Unsolved.security.JwtAuthFilter;
import com.Project.Unsolved.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // ✅ PUBLIC APIs
                        .requestMatchers(
                                "/auth/**",
                                "/h2-console/**",
                                "/error",
                                "/problems/**",
                                "/solutions/problem/**"
                        ).permitAll()

                        // ✅ CREATE PROBLEM → ANY LOGGED-IN USER
                        .requestMatchers(HttpMethod.POST, "/problems").authenticated()

                        // ✅ SUBMIT SOLUTION → ONLY STUDENT
                        .requestMatchers(HttpMethod.POST, "/solutions").hasRole("STUDENT")

                        // ✅ ACCEPT SOLUTION → AUTH (checked in service)
                        .requestMatchers(HttpMethod.POST, "/solutions/*/accept").authenticated()

                        // 🔒 Everything else
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        new JwtAuthFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                )

                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:63342"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}