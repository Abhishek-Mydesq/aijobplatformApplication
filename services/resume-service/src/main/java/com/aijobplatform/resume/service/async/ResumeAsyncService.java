package com.aijobplatform.resume.service.async;

import com.aijobplatform.resume.client.AiServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeAsyncService {

    private final AiServiceClient aiServiceClient;

    @Async
    public void analyzeResume(Long resumeId) {

        try {
            aiServiceClient.analyzeResume(resumeId);
        } catch (Exception e) {
            System.out.println("AI async failed: " + e.getMessage());
        }

    }
}