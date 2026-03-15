package com.aijobplatform.ai.repository;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ResumeAnalysisRepository
        extends JpaRepository<ResumeAnalysis, Long> {
    Optional<ResumeAnalysis> findByResumeId(Long resumeId);
    List<ResumeAnalysis> findByUserId(Long userId);
    Page<ResumeAnalysis> findAll(Pageable pageable);
}