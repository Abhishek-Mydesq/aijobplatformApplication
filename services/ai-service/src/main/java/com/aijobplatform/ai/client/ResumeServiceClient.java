package com.aijobplatform.ai.client;

import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.ResumeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resume-service")
public interface ResumeServiceClient {

    @GetMapping("/api/resumes/{id}")
    APIResponse<ResumeResponse> getResumeById(@PathVariable Long id);

}