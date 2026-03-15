package com.aijobplatform.ai.dto;
import lombok.Data;
import java.util.List;

@Data
public class AiResumeResponse {
    private Long analysisId;
    private Integer resumeScore;
    private List<String> skills;
    private Integer experienceYears;
    private String summary;
    private String status;
    private String message;
}