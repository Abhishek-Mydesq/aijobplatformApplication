package com.aijobplatform.job.repository;
import com.aijobplatform.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String title,
            String location,
            Pageable pageable
    );

}