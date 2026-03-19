package com.aijobplatform.ai.service.kafka;

import com.aijobplatform.kafka.config.KafkaTopics;
import com.aijobplatform.kafka.dto.ResumeUploadedEvent;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeEventConsumer {

    private final AiResumeService aiResumeService;

    @KafkaListener(
            topics = KafkaTopics.RESUME_UPLOADED,
            groupId = "ai-group"
    )
    public void handleResumeUploaded(ResumeUploadedEvent event) {

        System.out.println("Kafka received resume: " + event.getResumeId());

        aiResumeService.analyzeResume(event.getResumeId());

    }

}