package com.aijobplatform.application.client;
import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.ResumeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "resume-service")
public interface ResumeServiceClient {

    @GetMapping("/api/resumes/{id}")
    ApiResponse<ResumeResponse> getResumeById(@PathVariable Long id);

}