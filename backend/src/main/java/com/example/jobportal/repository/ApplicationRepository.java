package com.example.jobportal.repository;

import com.example.jobportal.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByCandidateIdOrderByAppliedAtDesc(Long candidateId);
    List<JobApplication> findByJobCompanyOrderByAppliedAtDesc(String company);
    List<JobApplication> findByJobIdOrderByAppliedAtDesc(Long jobId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
}
