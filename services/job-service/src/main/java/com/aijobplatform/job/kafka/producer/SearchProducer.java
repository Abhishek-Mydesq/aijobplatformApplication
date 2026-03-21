package com.aijobplatform.job.kafka.producer;

import com.aijobplatform.job.kafka.dto.SearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class SearchProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "search-topic";

    public void send(SearchEvent event) {

        kafkaTemplate.send(
                TOPIC,
                event
        );

    }

}