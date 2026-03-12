package com.aijobplatform.ai.controller;
import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.JobRecommendationResponse;
import com.aijobplatform.ai.service.JobRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final JobRecommendationService recommendationService;

    @GetMapping("/recommend/{analysisId}")
    public ResponseEntity<APIResponse<List<JobRecommendationResponse>>> recommendJobs(
            @PathVariable Long analysisId) {

        List<JobRecommendationResponse> jobs =
                recommendationService.recommendJobs(analysisId);

        APIResponse<List<JobRecommendationResponse>> response =
                new APIResponse<>(true, "Recommended jobs fetched", jobs);

        return ResponseEntity.ok(response);
    }
}