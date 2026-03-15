package com.aijobplatform.ai.service;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.dto.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AiResumeService {
    AiResumeResponse parseResume(MultipartFile file);
    AiResumeResponse analyzeResume(Long resumeId);
    AiResumeResponse getByResumeId(Long resumeId);
    AiResumeResponse getById(Long id);
    List<AiResumeResponse> getByUserId(Long userId);
    PageResponse<AiResumeResponse> getAll(int page, int size);
    AiResumeResponse reanalyze(Long resumeId);
}