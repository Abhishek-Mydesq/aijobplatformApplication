package com.aijobplatform.ai.service.kafka;

import com.aijobplatform.ai.service.kafka.dto.SearchEvent;
import com.aijobplatform.kafka.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(Long resumeId) {

        SearchEvent event = SearchEvent.builder()
                .refId(resumeId)
                .refType("RESUME")
                .title("Resume " + resumeId)
                .content("resume uploaded")
                .tags("resume")
                .build();

        kafkaTemplate.send(
                KafkaTopics.SEARCH_TOPIC,
                event
        );
    }
}