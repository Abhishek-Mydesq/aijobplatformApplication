package com.aijobplatform.job.dto;
import lombok.*;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private Double salary;
    private String jobType;
    private String experienceLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}