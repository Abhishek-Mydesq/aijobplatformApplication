package com.aijobplatform.resume.dto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResumeResponse {

    private Long id;
    private Long userId;
    private String fileName;
    private String fileType;
    private String filePath;
    private Long fileSize;
    private Integer version;
    private Boolean isDefault;
    private String status;
    private String analysisStatus;
    private LocalDateTime createdAt;
}