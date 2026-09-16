package com.example.jobportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank private String title;
    @NotBlank private String company;
    @NotBlank private String location;
    @NotBlank private String type;
    @NotBlank @Column(columnDefinition = "TEXT") private String description;
    @NotBlank @Column(columnDefinition = "TEXT") private String requirements;
    @Column(nullable = false) private LocalDateTime createdAt;
    @ManyToOne private User postedBy;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Job() {}
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String value) { title = value; }
    public String getCompany() { return company; }
    public void setCompany(String value) { company = value; }
    public String getLocation() { return location; }
    public void setLocation(String value) { location = value; }
    public String getType() { return type; }
    public void setType(String value) { type = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { description = value; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String value) { requirements = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User value) { postedBy = value; }
}
