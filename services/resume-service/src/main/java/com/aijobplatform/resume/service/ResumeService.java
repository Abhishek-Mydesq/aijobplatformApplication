package com.aijobplatform.resume.service;
import com.aijobplatform.resume.dto.PageResponse;
import com.aijobplatform.resume.dto.ResumeResponse;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
public interface ResumeService {

    ResumeResponse uploadResume(Long userId, MultipartFile file);
    PageResponse<ResumeResponse> getResumesByUser(Long userId, int page, int size);
    ResumeResponse getResumeById(Long id);
    void deleteResume(Long id);
    Resource downloadResume(Long id);
}