package com.aijobplatform.notification.dto.response;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.entity.enums.NotificationType;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private Boolean isRead;
    private LocalDateTime createdAt;

}