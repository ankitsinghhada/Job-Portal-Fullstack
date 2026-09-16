package com.example.jobportal.security;

import com.example.jobportal.repository.UserRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
    @Bean UserDetailsService userDetailsService(UserRepository users) { return email -> users.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found")); }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean AuthenticationProvider authenticationProvider(UserDetailsService service, PasswordEncoder encoder) { DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); provider.setUserDetailsService(service); provider.setPasswordEncoder(encoder); return provider; }
    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager(); }
    @Bean SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwt, AuthenticationProvider provider) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> {}).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/uploads/**", "/api/jobs", "/api/jobs/**").permitAll().anyRequest().authenticated()).authenticationProvider(provider).addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
