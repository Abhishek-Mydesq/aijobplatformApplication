package com.aijobplatform.ai.dto;

import lombok.Data;

@Data
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private Double salary;
    private String jobType;
    private String requiredSkills;
    private String experienceLevel;
}