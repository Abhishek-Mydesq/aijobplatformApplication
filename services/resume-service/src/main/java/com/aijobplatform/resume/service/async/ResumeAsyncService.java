package com.aijobplatform.resume.service.async;

import com.aijobplatform.resume.client.AiServiceClient;
import com.aijobplatform.resume.entity.AnalysisStatus;
import com.aijobplatform.resume.entity.Resume;
import com.aijobplatform.resume.entity.ResumeStatus;
import com.aijobplatform.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeAsyncService {

    private final AiServiceClient aiServiceClient;
    private final ResumeRepository resumeRepository;

    @Async
    public void analyzeResume(Long resumeId) {

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow();

        try {

            resume.setStatus(ResumeStatus.ANALYZING);
            resume.setAnalysisStatus(AnalysisStatus.PROCESSING);

            resumeRepository.save(resume);

            aiServiceClient.analyzeResume(resumeId);

            resume.setStatus(ResumeStatus.READY);
            resume.setAnalysisStatus(AnalysisStatus.DONE);

        } catch (Exception e) {

            resume.setStatus(ResumeStatus.FAILED);
            resume.setAnalysisStatus(AnalysisStatus.FAILED);

        }

        resumeRepository.save(resume);
    }
}