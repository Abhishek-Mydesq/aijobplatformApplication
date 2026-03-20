package com.aijobplatform.notification.kafka.consumer;

import com.aijobplatform.notification.entity.Notification;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.kafka.dto.NotificationEvent;
import com.aijobplatform.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService service;

    private final ObjectMapper mapper = new ObjectMapper();


    @KafkaListener(
            topics = "notification-topic",
            groupId = "notification-group"
    )
    public void consume(String message) {

        try {

            NotificationEvent event =
                    mapper.readValue(message, NotificationEvent.class);

            log.info(
                    "Notification event received user={} type={}",
                    event.getUserId(),
                    event.getType()
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

        } catch (Exception e) {

            log.error("Kafka consume error", e);

        }

    }

}