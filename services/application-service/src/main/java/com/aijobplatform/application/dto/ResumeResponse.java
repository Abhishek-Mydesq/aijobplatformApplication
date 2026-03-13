package com.aijobplatform.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {

    private Long id;
    private Long userId;
    private String fileName;
    private String fileType;
    private String filePath;
    private LocalDateTime createdAt;

}