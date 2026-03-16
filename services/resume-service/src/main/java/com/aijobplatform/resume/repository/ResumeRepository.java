package com.aijobplatform.resume.repository;

import com.aijobplatform.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findByUserId(Long userId, Pageable pageable);
    long countByUserId(Long userId);
    Optional<Resume> findByUserIdAndIsDefaultTrue(Long userId);

}