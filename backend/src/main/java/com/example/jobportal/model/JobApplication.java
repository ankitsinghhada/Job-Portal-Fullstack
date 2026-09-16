package com.example.jobportal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "candidate_id"}))
public class JobApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Job job;
    @ManyToOne(optional = false) private User candidate;
    @Column(nullable = false) private String status = "APPLIED";
    private String resumePath;
    @Column(nullable = false) private LocalDateTime appliedAt;
    @PrePersist void onCreate() { appliedAt = LocalDateTime.now(); }
    public JobApplication() {}
    public JobApplication(Job job, User candidate, String resumePath) { this.job = job; this.candidate = candidate; this.resumePath = resumePath; }
    public Long getId() { return id; }
    public Job getJob() { return job; }
    public User getCandidate() { return candidate; }
    public String getStatus() { return status; }
    public void setStatus(String value) { status = value; }
    public String getResumePath() { return resumePath; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
}
