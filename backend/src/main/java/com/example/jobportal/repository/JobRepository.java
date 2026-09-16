package com.example.jobportal.repository;

import com.example.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Query("select j from Job j where lower(j.title) like lower(concat('%', :query, '%')) or lower(j.company) like lower(concat('%', :query, '%')) or lower(j.location) like lower(concat('%', :query, '%'))")
    List<Job> search(@Param("query") String query);
    List<Job> findByPostedByIdOrderByCreatedAtDesc(Long recruiterId);
}
