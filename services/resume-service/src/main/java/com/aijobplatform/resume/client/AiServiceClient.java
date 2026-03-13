package com.aijobplatform.resume.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ai-service")
public interface AiServiceClient {
    @PostMapping("/api/ai/analyze/{resumeId}")
    void analyzeResume(@PathVariable Long resumeId);
}