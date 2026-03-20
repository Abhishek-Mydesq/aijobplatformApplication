package com.aijobplatform.notification.repository;
import com.aijobplatform.notification.entity.Notification;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.entity.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserId(
            Long userId,
            Pageable pageable
    );

    long countByUserId(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);

    Page<Notification> findByUserIdAndIsReadFalse(
            Long userId,
            Pageable pageable
    );

    long countByStatus(NotificationStatus status);

    long countByType(NotificationType type);

}