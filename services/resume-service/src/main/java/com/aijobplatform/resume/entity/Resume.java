package com.aijobplatform.resume.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "resumes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resume extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String fileName;
    private String fileType;
    private String filePath;
    private Long fileSize;
    @Column(nullable = false)
    private Integer version;
    @Column(nullable = false)
    private Boolean isDefault;
    @Enumerated(EnumType.STRING)
    private ResumeStatus status;
    @Enumerated(EnumType.STRING)
    private AnalysisStatus analysisStatus;
}