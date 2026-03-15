package com.aijobplatform.ai.service.impl;

import com.aijobplatform.ai.client.JobServiceClient;
import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.JobResponse;
import com.aijobplatform.ai.dto.JobRecommendationResponse;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import com.aijobplatform.ai.exception.ResourceNotFoundException;
import com.aijobplatform.ai.repository.ResumeAnalysisRepository;
import com.aijobplatform.ai.service.JobRecommendationService;
import com.aijobplatform.ai.service.OpenAIService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRecommendationServiceImpl implements JobRecommendationService {

    private final ResumeAnalysisRepository repository;
    private final JobServiceClient jobServiceClient;
    private final OpenAIService openAIService;


    @Override
    public List<JobRecommendationResponse> recommendJobs(Long analysisId) {

        ResumeAnalysis analysis = repository.findById(analysisId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume analysis not found"));

        // ✅ status check
        if (!"DONE".equals(analysis.getStatus())) {
            throw new RuntimeException("Analysis not completed yet");
        }

        // ✅ safe skills
        List<String> resumeSkills =
                analysis.getSkills() == null
                        ? List.of()
                        : Arrays.stream(
                                analysis.getSkills().split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .toList();

        int resumeScore =
                analysis.getResumeScore() == null
                        ? 0
                        : analysis.getResumeScore();


        APIResponse<List<JobResponse>> response =
                jobServiceClient.getAllJobs();

        List<JobResponse> jobs = response.getData();

        if (jobs == null) {
            return List.of();
        }

        // ✅ limit jobs to avoid slow AI calls
        jobs = jobs.stream()
                .limit(20)
                .toList();

        List<JobRecommendationResponse> recommendations =
                new ArrayList<>();


        for (JobResponse job : jobs) {

            if (job.getRequiredSkills() == null
                    || job.getRequiredSkills().isBlank()) {
                continue;
            }

            List<String> jobSkills =
                    Arrays.stream(
                                    job.getRequiredSkills().split(","))
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();


            // ---------- skill match ----------
            long matched = jobSkills.stream()
                    .filter(resumeSkills::contains)
                    .count();

            int skillScore =
                    jobSkills.isEmpty()
                            ? 0
                            : (int) ((matched * 100.0) / jobSkills.size());


            // ---------- resume score ----------
            int resumeScoreWeight = resumeScore / 5;


            // ---------- keyword bonus ----------
            int keywordBonus = 0;

            for (String skill : resumeSkills) {

                if (skill.contains("java")
                        || skill.contains("spring")
                        || skill.contains("microservices")
                        || skill.contains("docker")
                        || skill.contains("aws")) {

                    keywordBonus += 2;
                }
            }


            // ---------- AI score ----------
            int aiScore =
                    askAIScore(analysis, job);


            // ---------- final ----------
            int finalScore =
                    (int) (
                            (skillScore * 0.4)
                                    + (resumeScoreWeight * 0.2)
                                    + keywordBonus
                                    + (aiScore * 0.4)
                    );

            // ✅ safe range
            finalScore =
                    Math.max(0,
                            Math.min(100, finalScore));


            if (finalScore > 0) {

                recommendations.add(
                        new JobRecommendationResponse(
                                job.getId(),
                                job.getTitle(),
                                finalScore
                        )
                );
            }
        }


        return recommendations.stream()
                .sorted(
                        Comparator.comparingInt(
                                JobRecommendationResponse::getMatchScore
                        ).reversed()
                )
                .collect(Collectors.toList());
    }


    // ================= AI SCORE =================

    private int askAIScore(
            ResumeAnalysis analysis,
            JobResponse job
    ) {

        try {

            String prompt =
                    "Rate job match 0-100.\n" +
                            "Resume skills: " + analysis.getSkills() + "\n" +
                            "Experience: " + analysis.getExperienceYears() + "\n" +
                            "Summary: " + analysis.getSummary() + "\n" +
                            "Job title: " + job.getTitle() + "\n" +
                            "Required skills: " + job.getRequiredSkills() + "\n" +
                            "Return only number";

            String res =
                    openAIService.askAI(prompt);

            if (res == null) return 0;

            String num =
                    res.replaceAll("[^0-9]", "");

            if (num.isEmpty()) return 0;

            try {
                return Integer.parseInt(num);
            } catch (Exception e) {
                return 0;
            }

        } catch (Exception e) {

            return 0;
        }
    }
}