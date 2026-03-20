package com.aijobplatform.notification.common;
import com.aijobplatform.notification.dto.response.NotificationResponse;
import com.aijobplatform.notification.entity.Notification;
public class NotificationMapper {

    public static NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .status(n.getStatus())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }

}