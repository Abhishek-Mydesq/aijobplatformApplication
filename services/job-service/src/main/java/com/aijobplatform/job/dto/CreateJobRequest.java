package com.aijobplatform.job.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateJobRequest {
    private String title;
    private String description;
    private String companyName;
    private String location;
    private Double salary;
    private String jobType;
    private String experienceLevel;
}