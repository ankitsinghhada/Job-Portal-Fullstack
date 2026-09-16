package com.example.jobportal.controller;

import com.example.jobportal.model.User;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth") @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {
    private final UserRepository users; private final PasswordEncoder encoder; private final AuthenticationManager auth; private final JwtService jwt;
    public AuthController(UserRepository users, PasswordEncoder encoder, AuthenticationManager auth, JwtService jwt) { this.users = users; this.encoder = encoder; this.auth = auth; this.jwt = jwt; }
    public record Credentials(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password) {}
    public record Registration(@Email @NotBlank String email, @NotBlank @Size(min = 6) String password, @NotBlank String name, @Pattern(regexp = "CANDIDATE|RECRUITER") String role) {}
    public record AuthResponse(String token, String name, String email, String role) {}
    @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody Registration request) {
        if (users.existsByEmail(request.email())) return ResponseEntity.badRequest().body("Email is already registered");
        User user = users.save(new User(request.email(), encoder.encode(request.password()), request.name(), request.role()));
        return ResponseEntity.ok(response(user));
    }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody Credentials request) {
        auth.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        return response(users.findByEmail(request.email()).orElseThrow());
    }
    private AuthResponse response(UserDetails details) { User user = (User) details; return new AuthResponse(jwt.generate(user), user.getName(), user.getUsername(), user.getRole()); }
}
