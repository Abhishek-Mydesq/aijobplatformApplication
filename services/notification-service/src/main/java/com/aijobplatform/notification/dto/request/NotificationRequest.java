package com.aijobplatform.notification.dto.request;

import com.aijobplatform.notification.entity.enums.NotificationPriority;
import com.aijobplatform.notification.entity.enums.NotificationType;
import lombok.*;

@Getter
@Setter
public class NotificationRequest {

    private Long userId;

    private String title;

    private String message;

    private NotificationType type;

    private NotificationPriority priority;

    private String sourceService;

    private String metadata;

}
