package com.aijobplatform.job.entity;

import jakarta.persistence.*;
import lombok.*;
import com.aijobplatform.job.entity.BaseEntity;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    private String companyName;
    private String location;
    private Double salary;
    private String jobType;
    private String experienceLevel;
    @Column(columnDefinition = "TEXT")
    private String requiredSkills;
}