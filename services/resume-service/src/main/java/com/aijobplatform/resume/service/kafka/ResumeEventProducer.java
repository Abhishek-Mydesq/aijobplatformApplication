package com.aijobplatform.resume.service.kafka;

import com.aijobplatform.kafka.config.KafkaTopics;
import com.aijobplatform.kafka.dto.ResumeUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendResumeUploaded(ResumeUploadedEvent event) {

        kafkaTemplate.send(
                KafkaTopics.RESUME_UPLOADED,
                event
        );

    }

}