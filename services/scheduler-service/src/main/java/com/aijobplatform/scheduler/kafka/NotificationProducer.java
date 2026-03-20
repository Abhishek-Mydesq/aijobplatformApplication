package com.aijobplatform.scheduler.kafka;

import com.aijobplatform.scheduler.dto.NotificationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOPIC = "notification-topic";

    public void send(NotificationEvent event) {

        try {

            String json = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(TOPIC, json);

            log.info("Sending notification event {}", json);

        } catch (Exception e) {

            log.error("Kafka send error", e);

        }

    }

}