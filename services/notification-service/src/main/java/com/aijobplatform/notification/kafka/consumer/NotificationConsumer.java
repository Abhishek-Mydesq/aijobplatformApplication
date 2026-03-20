package com.aijobplatform.notification.kafka.consumer;
import com.aijobplatform.notification.entity.Notification;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.kafka.dto.NotificationEvent;
import com.aijobplatform.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService service;

    @KafkaListener(
            topics = "notification-topic",
            groupId = "notification-group"
    )
    public void consume(NotificationEvent event) {

        log.info(
                "Notification event received user={} type={} source={}",
                event.getUserId(),
                event.getType(),
                event.getSourceService()
        );

        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .title(event.getTitle())
                .message(event.getMessage())
                .type(event.getType())
                .priority(event.getPriority())
                .sourceService(event.getSourceService())
                .metadata(event.getMetadata())
                .status(NotificationStatus.CREATED)
                .isRead(false)
                .build();


        service.save(notification);

    }

}