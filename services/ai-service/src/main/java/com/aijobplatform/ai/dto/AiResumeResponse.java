package com.aijobplatform.ai.dto;
import lombok.Data;
import java.util.List;

@Data
public class AiResumeResponse {

    private Long analysisId;

    private List<String> skills;

    private int resumeScore;

    private String message;

}