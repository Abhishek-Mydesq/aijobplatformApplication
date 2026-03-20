package com.aijobplatform.notification.dto.response;
import com.aijobplatform.notification.entity.enums.NotificationPriority;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.entity.enums.NotificationType;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private NotificationPriority priority;
    private Boolean isRead;
    private LocalDateTime readAt;
    private String sourceService;
    private String metadata;
    private LocalDateTime createdAt;

}