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

    private Long resumeId;

    @Column(columnDefinition = "TEXT")
    private String skills;

    private Integer resumeScore;
}