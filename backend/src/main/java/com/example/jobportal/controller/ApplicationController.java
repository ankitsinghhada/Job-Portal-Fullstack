package com.example.jobportal.controller;

import com.example.jobportal.model.*;
import com.example.jobportal.repository.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.List;

@RestController @RequestMapping("/api/applications") @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ApplicationController {
    private final ApplicationRepository applications; private final JobRepository jobs;
    private final Path uploadDirectory = Paths.get("uploads");
    public ApplicationController(ApplicationRepository applications, JobRepository jobs) { this.applications = applications; this.jobs = jobs; }
    @PostMapping(value = "/jobs/{jobId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(@PathVariable Long jobId, @RequestPart(required = false) MultipartFile resume, @AuthenticationPrincipal User candidate) throws IOException {
        if (!"CANDIDATE".equals(candidate.getRole())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only candidates can apply");
        if (applications.existsByJobIdAndCandidateId(jobId, candidate.getId())) return ResponseEntity.badRequest().body("You already applied to this job");
        Job job = jobs.findById(jobId).orElse(null); if (job == null) return ResponseEntity.notFound().build();
        String path = null;
        if (resume != null && !resume.isEmpty()) { Files.createDirectories(uploadDirectory); String filename = candidate.getId() + "-" + System.currentTimeMillis() + "-" + Path.of(resume.getOriginalFilename()).getFileName(); Files.copy(resume.getInputStream(), uploadDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING); path = filename; }
        return ResponseEntity.ok(applications.save(new JobApplication(job, candidate, path)));
    }
    @GetMapping("/mine") public List<JobApplication> mine(@AuthenticationPrincipal User candidate) { return applications.findByCandidateIdOrderByAppliedAtDesc(candidate.getId()); }
    @GetMapping("/job/{jobId}") public ResponseEntity<?> forJob(@PathVariable Long jobId, @AuthenticationPrincipal User recruiter) { Job job = jobs.findById(jobId).orElse(null); if (job == null) return ResponseEntity.notFound().build(); if (!owns(job, recruiter)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); return ResponseEntity.ok(applications.findByJobIdOrderByAppliedAtDesc(jobId)); }
    @PatchMapping("/{id}/status") public ResponseEntity<?> status(@PathVariable Long id, @RequestParam String value, @AuthenticationPrincipal User recruiter) { if (!List.of("APPLIED", "REVIEWING", "INTERVIEW", "REJECTED", "HIRED").contains(value)) return ResponseEntity.badRequest().body("Invalid status"); return applications.findById(id).map(application -> { if (!owns(application.getJob(), recruiter)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); application.setStatus(value); return ResponseEntity.ok(applications.save(application)); }).orElseGet(() -> ResponseEntity.notFound().build()); }
    private boolean owns(Job job, User recruiter) { return recruiter != null && "RECRUITER".equals(recruiter.getRole()) && job.getPostedBy() != null && job.getPostedBy().getId().equals(recruiter.getId()); }
}
