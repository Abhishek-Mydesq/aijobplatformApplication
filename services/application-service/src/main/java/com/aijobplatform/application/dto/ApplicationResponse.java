package com.aijobplatform.application.dto;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class ApplicationResponse {
    private Long id;
    private Long userId;
    private Long jobId;
    private Long resumeId;
    private String status;
    private LocalDateTime createdAt;

}