package com.aijobplatform.application.repository;

import com.aijobplatform.application.dto.ApplicationStatus;
import com.aijobplatform.application.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByUserId(Long userId, Pageable pageable);
    Page<Application> findByJobId(Long jobId, Pageable pageable);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    long countByUserId(Long userId);
    long countByJobId(Long jobId);
    List<Application> findByUserIdAndStatus(
            Long userId,
            ApplicationStatus status
    );
    List<Application> findByJobIdAndStatus(
            Long jobId,
            ApplicationStatus status
    );
}