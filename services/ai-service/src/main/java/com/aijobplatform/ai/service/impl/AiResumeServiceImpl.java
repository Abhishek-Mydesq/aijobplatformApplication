package com.aijobplatform.ai.service.impl;

import com.aijobplatform.ai.common.ResumeParserUtil;
import com.aijobplatform.ai.common.ResumeScoreUtil;
import com.aijobplatform.ai.common.SkillExtractorUtil;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import com.aijobplatform.ai.exception.ResourceNotFoundException;
import com.aijobplatform.ai.repository.ResumeAnalysisRepository;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiResumeServiceImpl implements AiResumeService {

    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ModelMapper modelMapper;

    @Override
    public AiResumeResponse parseResume(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("Resume file not provided");
        }

        // 1️⃣ Extract text from resume
        String resumeText = ResumeParserUtil.extractText(file);

        if (resumeText == null || resumeText.isEmpty()) {
            throw new ResourceNotFoundException("Unable to extract text from resume");
        }

        // 2️⃣ Extract skills
        List<String> skills = SkillExtractorUtil.extractSkills(resumeText);

        // 3️⃣ Calculate resume score
        int score = ResumeScoreUtil.calculateScore(skills);

        // 4️⃣ Save analysis
        ResumeAnalysis entity = new ResumeAnalysis();

        entity.setSkills(String.join(",", skills));
        entity.setResumeScore(score);

        ResumeAnalysis saved = resumeAnalysisRepository.save(entity);

        // 5️⃣ Convert to DTO
        AiResumeResponse response = new AiResumeResponse();

        response.setAnalysisId(saved.getId());
        response.setSkills(skills);
        response.setResumeScore(score);
        response.setMessage("Resume analyzed successfully");

        return response;
    }
}