package com.aijobplatform.ai.service.impl;
import com.aijobplatform.ai.client.ResumeServiceClient;
import com.aijobplatform.ai.common.ResumeParserUtil;
import com.aijobplatform.ai.common.ResumeScoreUtil;
import com.aijobplatform.ai.common.SkillExtractorUtil;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.dto.ResumeResponse;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import com.aijobplatform.ai.exception.ResourceNotFoundException;
import com.aijobplatform.ai.repository.ResumeAnalysisRepository;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiResumeServiceImpl implements AiResumeService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ResumeServiceClient resumeServiceClient;
    private final ModelMapper modelMapper;

    // OLD METHOD (keep)
    @Override
    public AiResumeResponse parseResume(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("Resume file not provided");
        }

        String resumeText = ResumeParserUtil.extractText(file);

        List<String> skills = SkillExtractorUtil.extractSkills(resumeText);

        int score = ResumeScoreUtil.calculateScore(skills, resumeText);

        ResumeAnalysis entity = new ResumeAnalysis();

        entity.setSkills(String.join(",", skills));
        entity.setResumeScore(score);

        ResumeAnalysis saved = resumeAnalysisRepository.save(entity);

        AiResumeResponse response = new AiResumeResponse();

        response.setAnalysisId(saved.getId());
        response.setSkills(skills);
        response.setResumeScore(score);
        response.setMessage("Resume analyzed successfully");

        return response;
    }


    // ✅ NEW PRODUCTION METHOD
    @Override
    public AiResumeResponse analyzeResume(Long resumeId) {

        // ✅ 0 check if already analyzed
        ResumeAnalysis existing =
                resumeAnalysisRepository.findByResumeId(resumeId)
                        .orElse(null);

        if (existing != null) {

            AiResumeResponse response = new AiResumeResponse();

            response.setAnalysisId(existing.getId());
            response.setSkills(
                    List.of(existing.getSkills().split(","))
            );
            response.setResumeScore(existing.getResumeScore());
            response.setMessage("Resume already analyzed");

            return response;
        }


        // 1️⃣ call resume-service
        ResumeResponse resume =
                resumeServiceClient.getResumeById(resumeId).getData();

        if (resume == null) {
            throw new ResourceNotFoundException("Resume not found");
        }

        String filePath = resume.getFilePath();

        File file = new File(filePath);

        if (!file.exists()) {
            throw new ResourceNotFoundException("Resume file not found on disk");
        }

        // 2️⃣ extract text
        String resumeText = ResumeParserUtil.extractText(file);

        // 3️⃣ extract skills
        List<String> skills = SkillExtractorUtil.extractSkills(resumeText);

        // 4️⃣ score
        int score = ResumeScoreUtil.calculateScore(skills, resumeText);

        // 5️⃣ save analysis
        ResumeAnalysis entity = new ResumeAnalysis();

        entity.setResumeId(resume.getId());
        entity.setUserId(resume.getUserId());
        entity.setSkills(String.join(",", skills));
        entity.setResumeScore(score);

        ResumeAnalysis saved = resumeAnalysisRepository.save(entity);

        // 6️⃣ response
        AiResumeResponse response = new AiResumeResponse();

        response.setAnalysisId(saved.getId());
        response.setSkills(skills);
        response.setResumeScore(score);
        response.setMessage("Resume analyzed successfully");

        return response;
    }
}