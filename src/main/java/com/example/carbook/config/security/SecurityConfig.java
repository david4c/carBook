package com.example.carbook.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/users/register", "/api/v1/users/login", "login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getOutputStream().println("{ \"error\": \"Unauthorized\" }");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getOutputStream().println("{ \"error\": \"Forbidden\" }");
                                })
                )
                .authenticationManager(authenticationManager);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}