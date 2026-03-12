package com.aijobplatform.ai.client;
import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(name = "job-service")
public interface JobServiceClient {

    @GetMapping("/api/jobs")
    APIResponse<List<JobResponse>> getAllJobs();

}