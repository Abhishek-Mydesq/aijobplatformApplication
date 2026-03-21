package com.aijobplatform.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopics {

    public static final String RESUME_UPLOADED = "resume-uploaded";
    public static final String ANALYSIS_DONE = "analysis-done";
    public static final String JOB_CREATED = "job-created";
    public static final String APPLICATION_CREATED = "application-created";
    public static final String USER_REGISTERED = "user-registered";
    public static final String SEARCH_TOPIC = "search-topic";
    public static final String NOTIFICATION_TOPIC = "notification-topic";

    @Bean
    public NewTopic resumeUploadedTopic() {
        return new NewTopic(RESUME_UPLOADED, 1, (short) 1);
    }

    @Bean
    public NewTopic analysisDoneTopic() {
        return new NewTopic(ANALYSIS_DONE, 1, (short) 1);
    }

    @Bean
    public NewTopic searchTopic() {
        return new NewTopic(SEARCH_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(NOTIFICATION_TOPIC, 1, (short) 1);
    }
}