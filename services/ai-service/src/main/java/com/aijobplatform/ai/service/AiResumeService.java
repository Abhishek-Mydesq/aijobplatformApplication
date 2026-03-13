package com.aijobplatform.ai.service;
import com.aijobplatform.ai.dto.AiResumeResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AiResumeService {
    AiResumeResponse parseResume(MultipartFile file);
    AiResumeResponse analyzeResume(Long resumeId);
}