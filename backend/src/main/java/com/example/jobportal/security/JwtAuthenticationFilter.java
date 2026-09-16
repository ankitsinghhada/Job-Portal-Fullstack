package com.example.jobportal.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt; private final UserDetailsService users;
    public JwtAuthenticationFilter(JwtService jwt, UserDetailsService users) { this.jwt = jwt; this.users = users; }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, java.io.IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) { try { String token = header.substring(7); var user = users.loadUserByUsername(jwt.username(token)); if (jwt.valid(token, user)) SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())); } catch (RuntimeException ignored) {} }
        chain.doFilter(request, response);
    }
}
