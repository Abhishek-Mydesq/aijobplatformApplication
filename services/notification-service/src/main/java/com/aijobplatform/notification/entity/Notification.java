package com.aijobplatform.notification.entity;
import com.aijobplatform.notification.common.BaseEntity;
import com.aijobplatform.notification.entity.enums.NotificationPriority;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_user", columnList = "userId"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_type", columnList = "type"),
                @Index(name = "idx_read", columnList = "isRead"),
                @Index(name = "idx_created", columnList = "createdAt"),
                @Index(name = "idx_priority", columnList = "priority")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    private NotificationPriority priority;

    private Boolean isRead;

    private LocalDateTime readAt;

    private String sourceService;

    @Column(length = 2000)
    private String metadata;

}