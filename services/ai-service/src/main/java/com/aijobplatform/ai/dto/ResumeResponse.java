package com.aijobplatform.ai.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResumeResponse {

    private Long id;
    private Long userId;
    private String fileName;
    private String fileType;
    private String filePath;
    private LocalDateTime createdAt;

}