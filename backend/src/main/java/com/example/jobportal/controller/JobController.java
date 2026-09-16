package com.example.jobportal.controller;

import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class JobController {
    private final JobRepository jobs;
    public JobController(JobRepository jobs) { this.jobs = jobs; }

    @GetMapping public List<Job> list(@RequestParam(required = false, defaultValue = "") String query) {
        return query.isBlank() ? jobs.findAll() : jobs.search(query);
    }
    @GetMapping("/mine") public ResponseEntity<?> mine(@AuthenticationPrincipal com.example.jobportal.model.User recruiter) {
        if (recruiter == null || !"RECRUITER".equals(recruiter.getRole())) return ResponseEntity.status(403).body("Recruiter account required");
        return ResponseEntity.ok(jobs.findByPostedByIdOrderByCreatedAtDesc(recruiter.getId()));
    }
    @GetMapping("/{id}") public ResponseEntity<Job> get(@PathVariable Long id) {
        return jobs.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody Job job, @AuthenticationPrincipal com.example.jobportal.model.User recruiter) { if (recruiter == null || !"RECRUITER".equals(recruiter.getRole())) return ResponseEntity.status(403).body("Recruiter account required"); job.setPostedBy(recruiter); return ResponseEntity.ok(jobs.save(job)); }
    @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Job incoming, @AuthenticationPrincipal com.example.jobportal.model.User recruiter) {
        if (recruiter == null || !"RECRUITER".equals(recruiter.getRole())) return ResponseEntity.status(403).body("Recruiter account required");
        return jobs.findById(id).map(job -> {
            job.setTitle(incoming.getTitle()); job.setCompany(incoming.getCompany()); job.setLocation(incoming.getLocation());
            job.setType(incoming.getType()); job.setDescription(incoming.getDescription()); job.setRequirements(incoming.getRequirements());
            return ResponseEntity.ok(jobs.save(job));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal com.example.jobportal.model.User recruiter) {
        if (recruiter == null || !"RECRUITER".equals(recruiter.getRole())) return ResponseEntity.status(403).body("Recruiter account required");
        if (!jobs.existsById(id)) return ResponseEntity.notFound().build();
        jobs.deleteById(id); return ResponseEntity.noContent().build();
    }
}
