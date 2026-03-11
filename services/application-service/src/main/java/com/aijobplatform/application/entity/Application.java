package com.aijobplatform.application.entity;

import com.aijobplatform.application.dto.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long jobId;
    private Long resumeId;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}