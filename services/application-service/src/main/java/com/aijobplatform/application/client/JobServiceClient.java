package com.aijobplatform.application.client;
import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "job-service")
public interface JobServiceClient {
    @GetMapping("/api/jobs/{id}")
    ApiResponse<JobResponse> getJobById(@PathVariable Long id);

}