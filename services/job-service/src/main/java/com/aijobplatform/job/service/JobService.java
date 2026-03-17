package com.aijobplatform.job.service;

import com.aijobplatform.job.dto.CreateJobRequest;
import com.aijobplatform.job.dto.JobResponse;
import com.aijobplatform.job.dto.PageResponse;

import java.util.List;

public interface JobService {
    JobResponse createJob(CreateJobRequest request);
    List<JobResponse> getAllJobs();
    JobResponse getJobById(Long id);
    JobResponse updateJob(Long id, CreateJobRequest request);
    void deleteJob(Long id);
    PageResponse<JobResponse> searchJobs(String title, String location, int page, int size);
    long countJobs();
    boolean exists(Long id);
    PageResponse<JobResponse> getAllJobsPage(int page, int size);
    PageResponse<JobResponse> getByJobType(
            String jobType,
            int page,
            int size
    );
    PageResponse<JobResponse> getByExperienceLevel(
            String level,
            int page,
            int size
    );
}