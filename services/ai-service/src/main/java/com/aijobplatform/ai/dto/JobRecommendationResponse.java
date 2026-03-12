package com.aijobplatform.ai.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobRecommendationResponse {

    private Long jobId;
    private String title;
    private int matchScore;

}