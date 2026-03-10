package com.aijobplatform.job.controller;

import com.aijobplatform.job.common.ApiResponse;
import com.aijobplatform.job.dto.CreateJobRequest;
import com.aijobplatform.job.dto.JobResponse;
import com.aijobplatform.job.dto.PageResponse;
import com.aijobplatform.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@RequestBody CreateJobRequest request) {
        JobResponse job = jobService.createJob(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job created successfully", job)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobResponse>>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Jobs fetched successfully", jobs)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        JobResponse job = jobService.getJobById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job fetched successfully", job)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable Long id,
            @RequestBody CreateJobRequest request) {
        JobResponse updatedJob = jobService.updateJob(id, request);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job updated successfully", updatedJob)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteJob(@PathVariable Long id) {

        jobService.deleteJob(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job deleted successfully", null)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<JobResponse>>> searchJobs(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        PageResponse<JobResponse> response =
                jobService.searchJobs(title, location, page, size);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Jobs fetched successfully", response)
        );
    }
}