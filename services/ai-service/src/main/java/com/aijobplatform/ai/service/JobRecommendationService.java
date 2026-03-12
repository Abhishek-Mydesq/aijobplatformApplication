package com.aijobplatform.ai.service;
import com.aijobplatform.ai.dto.JobRecommendationResponse;
import java.util.List;
public interface JobRecommendationService {
    List<JobRecommendationResponse> recommendJobs(Long analysisId);
}