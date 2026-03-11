package com.aijobplatform.application.controller;

import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.ApplyJobRequest;
import com.aijobplatform.application.dto.ApplicationResponse;
import com.aijobplatform.application.dto.PageResponse;
import com.aijobplatform.application.dto.UpdateApplicationStatusRequest;
import com.aijobplatform.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyJob(
            @RequestBody ApplyJobRequest request) {

        ApplicationResponse response = applicationService.applyJob(request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job applied successfully", response)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<ApplicationResponse> response =
                applicationService.getApplicationsByUser(userId, page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Applications fetched successfully", response)
        );
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationResponse>>> getApplicationsByJob(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<ApplicationResponse> response =
                applicationService.getApplicationsByJob(jobId, page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Applications fetched successfully", response)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody UpdateApplicationStatusRequest request) {

        ApplicationResponse response =
                applicationService.updateApplicationStatus(id, request.getStatus());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Application status updated", response)
        );
    }
}