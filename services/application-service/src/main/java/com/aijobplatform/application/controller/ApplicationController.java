package com.aijobplatform.application.controller;

import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.*;
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
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getById(
            @PathVariable Long id) {
        ApplicationResponse response =
                applicationService.getApplicationById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Application fetched", response)
        );
    }
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> countByUser(
            @PathVariable Long userId) {

        long count =
                applicationService.countByUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Count fetched", count)
        );
    }
    @GetMapping("/job/{jobId}/count")
    public ResponseEntity<ApiResponse<Long>> countByJob(
            @PathVariable Long jobId) {

        long count =
                applicationService.countByJob(jobId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Count fetched", count)
        );
    }
    @DeleteMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse<String>> withdraw(
            @PathVariable Long id) {

        applicationService.withdrawApplication(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Application withdrawn", null)
        );
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable ApplicationStatus status) {

        List<ApplicationResponse> response =
                applicationService.getByUserAndStatus(userId, status);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", response)
        );
    }
    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getByJobAndStatus(
            @PathVariable Long jobId,
            @PathVariable ApplicationStatus status) {

        List<ApplicationResponse> response =
                applicationService.getByJobAndStatus(jobId, status);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched", response)
        );
    }

}