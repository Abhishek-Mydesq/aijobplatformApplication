package com.aijobplatform.application.dto;
import lombok.Data;
@Data
public class ApplyJobRequest {
    private Long userId;
    private Long jobId;
    private Long resumeId;
}