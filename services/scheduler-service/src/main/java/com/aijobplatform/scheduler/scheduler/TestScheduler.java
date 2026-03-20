package com.aijobplatform.scheduler.scheduler;
import com.aijobplatform.scheduler.dto.NotificationEvent;
import com.aijobplatform.scheduler.kafka.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestScheduler {

    private final NotificationProducer producer;

    @Scheduled(fixedRate = 60000)
    public void testNotification() {

        NotificationEvent event =
                NotificationEvent.builder()
                        .userId(1L)
                        .title("Scheduler Event")
                        .message("Test from scheduler")
                        .type("SYSTEM")
                        .priority("NORMAL")
                        .sourceService("scheduler-service")
                        .metadata("test")
                        .build();

        producer.send(event);

        log.info("Scheduler event sent");

    }
}