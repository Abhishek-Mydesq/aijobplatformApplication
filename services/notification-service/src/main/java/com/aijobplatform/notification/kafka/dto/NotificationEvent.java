package com.aijobplatform.notification.kafka.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private Long userId;
    private String title;
    private String message;
    private String type;

}