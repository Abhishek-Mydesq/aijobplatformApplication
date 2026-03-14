package com.aijobplatform.ai.service.impl;

import com.aijobplatform.ai.client.JobServiceClient;
import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.JobResponse;
import com.aijobplatform.ai.dto.JobRecommendationResponse;
import com.aijobplatform.ai.entity.ResumeAnalysis;
import com.aijobplatform.ai.exception.ResourceNotFoundException;
import com.aijobplatform.ai.repository.ResumeAnalysisRepository;
import com.aijobplatform.ai.service.JobRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobRecommendationServiceImpl implements JobRecommendationService {

    private final ResumeAnalysisRepository repository;
    private final JobServiceClient jobServiceClient;

    @Override
    public List<JobRecommendationResponse> recommendJobs(Long analysisId) {

        ResumeAnalysis analysis = repository.findById(analysisId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume analysis not found"));

        List<String> resumeSkills = Arrays.stream(
                        analysis.getSkills().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        int resumeScore = analysis.getResumeScore();

        APIResponse<List<JobResponse>> response =
                jobServiceClient.getAllJobs();

        List<JobResponse> jobs = response.getData();

        List<JobRecommendationResponse> recommendations =
                new ArrayList<>();


        for (JobResponse job : jobs) {

            if (job.getRequiredSkills() == null) continue;

            List<String> jobSkills = Arrays.stream(
                            job.getRequiredSkills().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .toList();

            // ---------- skill match ----------
            long matched = jobSkills.stream()
                    .filter(resumeSkills::contains)
                    .count();

            int skillScore =
                    (int) ((matched * 100.0) / jobSkills.size());

            // ---------- resume score weight ----------
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

            // ---------- final score ----------
            int finalScore =
                    (int) (
                            (skillScore * 0.6)
                                    + (resumeScoreWeight * 0.2)
                                    + keywordBonus
                    );

            if (finalScore > 100) finalScore = 100;

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
}