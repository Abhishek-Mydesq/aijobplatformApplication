package com.aijobplatform.scheduler.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long userId;
    private String title;
    private String message;
    private String type;
    private String priority;
    private String sourceService;
    private String metadata;
}