package com.aijobplatform.ai.service.kafka;

import com.aijobplatform.ai.service.kafka.dto.SearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(SearchEvent event) {

        kafkaTemplate.send(
                "search-topic",
                event
        );

    }

}