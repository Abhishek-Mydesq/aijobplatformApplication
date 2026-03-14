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
import com.aijobplatform.ai.service.llm.LLMService;
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
    private final LLMService llmService;
    private final ModelMapper modelMapper;


    @Override
    public AiResumeResponse parseResume(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("Resume file not provided");
        }

        String resumeText = ResumeParserUtil.extractText(file);

        List<String> skills =
                SkillExtractorUtil.extractSkills(resumeText);

        int score =
                ResumeScoreUtil.calculateScore(
                        skills,
                        resumeText
                );

        ResumeAnalysis entity = new ResumeAnalysis();


        entity.setSkills(String.join(",", skills));
        entity.setResumeScore(score);
        ResumeAnalysis saved =
                resumeAnalysisRepository.save(entity);

        AiResumeResponse response =
                new AiResumeResponse();

        response.setAnalysisId(saved.getId());
        response.setSkills(skills);
        response.setResumeScore(score);
        response.setMessage("Resume analyzed successfully");

        return response;
    }


    @Override
    public AiResumeResponse analyzeResume(Long resumeId) {

        System.out.println("AI START " + resumeId);

        ResumeAnalysis entity =
                resumeAnalysisRepository
                        .findByResumeId(resumeId)
                        .orElse(new ResumeAnalysis());
        entity.setResumeId(resumeId);
        entity.setStatus("PROCESSING");

        try {

            // get resume
            ResumeResponse resume =
                    resumeServiceClient
                            .getResumeById(resumeId)
                            .getData();

            if (resume == null) {
                throw new ResourceNotFoundException("Resume not found");
            }

            File file = new File(resume.getFilePath());

            if (!file.exists()) {
                throw new ResourceNotFoundException("Resume file not found");
            }


            // extract text
            String resumeText =
                    ResumeParserUtil.extractText(file);


            // local skills
            List<String> skills =
                    SkillExtractorUtil.extractSkills(resumeText);


            // ---------- LLM ----------

            String prompt =
                    "You are a professional resume analyzer.\n" +
                            "Read the resume and return in this format:\n" +
                            "skills: skill1,skill2,skill3\n" +
                            "experience: number\n" +
                            "summary: short summary\n\n" +
                            "Resume:\n" +
                            resumeText;

            String aiResult = null;

            try {

                aiResult = llmService.ask(prompt);

            } catch (Exception e) {

                System.out.println("LLM ERROR: " + e.getMessage());
                entity.setStatus("FAILED");

            } finally {

                System.out.println("AI LLM DONE " + resumeId);

            }


            Integer experienceYears = 0;
            String summary = null;

            if (aiResult != null) {

                String lower = aiResult.toLowerCase();

                int skillIdx = lower.indexOf("skills:");

                if (skillIdx != -1) {

                    String part =
                            lower.substring(skillIdx + 7);

                    String[] arr =
                            part.split("\n")[0].split(",");

                    for (String s : arr) {

                        String skill = s.trim();

                        if (!skill.isEmpty()
                                && !skills.contains(skill)) {

                            skills.add(skill);
                        }
                    }
                }


                int expIdx = lower.indexOf("experience:");

                if (expIdx != -1) {

                    String part =
                            lower.substring(expIdx + 11)
                                    .split("\n")[0]
                                    .trim();

                    try {

                        String num =
                                part.replaceAll("[^0-9]", "");

                        if (!num.isEmpty() && num.length() <= 2) {
                            experienceYears = Integer.parseInt(num);
                        }

                    } catch (Exception ignored) {
                    }
                }


                int sumIdx = lower.indexOf("summary:");

                if (sumIdx != -1) {

                    summary =
                            aiResult.substring(sumIdx + 8).trim();
                }
            }


            int score =
                    ResumeScoreUtil.calculateScore(
                            skills,
                            resumeText
                    );


            entity.setResumeId(resume.getId());
            entity.setUserId(resume.getUserId());

            entity.setSkills(String.join(",", skills));
            entity.setResumeScore(score);
            entity.setExperienceYears(experienceYears);

            entity.setSummary(
                    summary != null ? summary : "AI not available"
            );

            entity.setAiResponse(
                    aiResult != null ? aiResult : "AI timeout"
            );

            if (!"FAILED".equals(entity.getStatus())) {
                entity.setStatus("DONE");
            }

            ResumeAnalysis saved =
                    resumeAnalysisRepository.save(entity);


            AiResumeResponse response =
                    new AiResumeResponse();

            response.setAnalysisId(saved.getId());
            response.setSkills(skills);
            response.setResumeScore(score);
            response.setMessage("Resume analyzed successfully");

            System.out.println("AI END " + resumeId);

            return response;

        } catch (Exception ex) {

            entity.setStatus("FAILED");
            resumeAnalysisRepository.save(entity);

            throw ex;
        }
    }
}