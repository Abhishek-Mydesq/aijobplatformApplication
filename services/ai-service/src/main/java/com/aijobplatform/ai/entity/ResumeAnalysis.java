package com.aijobplatform.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resume_analysis")
@Getter
@Setter
public class ResumeAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Column(name = "resume_id", unique = true)
    private Long resumeId;
    @Column(columnDefinition = "TEXT")
    private String skills;
    private Integer resumeScore;
    private Integer experienceYears;
    @Column(columnDefinition = "TEXT")
    private String summary;
    @Column(columnDefinition = "TEXT")
    private String aiResponse;
    @Column(name = "status")
    private String status;

}