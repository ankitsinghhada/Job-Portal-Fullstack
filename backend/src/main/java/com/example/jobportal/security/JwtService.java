package com.example.jobportal.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final byte[] key = "jobportal-local-development-secret-key-please-change".getBytes(StandardCharsets.UTF_8);
    public String generate(UserDetails user) {
        return Jwts.builder().subject(user.getUsername()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 86400000)).signWith(Keys.hmacShaKeyFor(key)).compact();
    }
    public String username(String token) { return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(key)).build().parseSignedClaims(token).getPayload().getSubject(); }
    public boolean valid(String token, UserDetails user) { return username(token).equals(user.getUsername()); }
}
