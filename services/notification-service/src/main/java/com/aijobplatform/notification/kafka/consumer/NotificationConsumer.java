package com.aijobplatform.notification.kafka.consumer;
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

        log.info("Notification event received for user={}",
                event.getUserId());

        service.save(
                event.getUserId(),
                event.getTitle(),
                event.getMessage(),
                event.getType()
        );

    }

}