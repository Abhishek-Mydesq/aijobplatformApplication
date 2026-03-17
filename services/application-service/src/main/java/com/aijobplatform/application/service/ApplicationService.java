package com.aijobplatform.application.service;

import com.aijobplatform.application.dto.ApplicationStatus;
import com.aijobplatform.application.dto.ApplyJobRequest;
import com.aijobplatform.application.dto.ApplicationResponse;
import com.aijobplatform.application.dto.PageResponse;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse applyJob(ApplyJobRequest request);
    PageResponse<ApplicationResponse> getApplicationsByUser(
            Long userId,
            int page,
            int size
    );
    PageResponse<ApplicationResponse> getApplicationsByJob(
            Long jobId,
            int page,
            int size
    );
    ApplicationResponse updateApplicationStatus(
            Long id,
            ApplicationStatus status
    );
    ApplicationResponse getApplicationById(Long id);
    long countByUser(Long userId);
    long countByJob(Long jobId);
    void withdrawApplication(Long id);
    List<ApplicationResponse> getByUserAndStatus(
            Long userId,
            ApplicationStatus status
    );
    List<ApplicationResponse> getByJobAndStatus(
            Long jobId,
            ApplicationStatus status
    );
}