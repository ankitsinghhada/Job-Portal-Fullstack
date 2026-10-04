package com.example.jobportal.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String email;
    @JsonIgnore @Column(nullable = false) private String password;
    @Column(nullable = false) private String name;
    @Column(nullable = false) private String role;
    @Column(length = 1000) private String skills;
    @Column(length = 500) private String githubUrl;
    @Column(length = 500) private String portfolioUrl;
    public User() {}
    public User(String email, String password, String name, String role) { this.email = email; this.password = password; this.name = name; this.role = role; }
    public User(String email, String password, String name, String role, String skills) { this.email = email; this.password = password; this.name = name; this.role = role; this.skills = skills; }
    public User(String email, String password, String name, String role, String skills, String githubUrl, String portfolioUrl) { this.email = email; this.password = password; this.name = name; this.role = role; this.skills = skills; this.githubUrl = githubUrl; this.portfolioUrl = portfolioUrl; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role)); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
