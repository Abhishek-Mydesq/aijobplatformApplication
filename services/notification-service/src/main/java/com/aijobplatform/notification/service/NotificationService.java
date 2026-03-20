package com.aijobplatform.notification.service;

import com.aijobplatform.notification.common.PageResponse;
import com.aijobplatform.notification.dto.response.NotificationResponse;
import com.aijobplatform.notification.entity.Notification;
import com.aijobplatform.notification.kafka.dto.NotificationEvent;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    // used by Kafka consumer
    Notification save(Notification notification);


    // used by API / test / manual
    NotificationResponse save(
            Long userId,
            String title,
            String message,
            String type
    );


    // ✅ used for bulk events
    List<NotificationResponse> saveBulk(
            List<NotificationEvent> events
    );


    PageResponse<NotificationResponse> getByUser(
            Long userId,
            int page,
            int size
    );


    PageResponse<NotificationResponse> getUnread(
            Long userId,
            int page,
            int size
    );


    PageResponse<NotificationResponse> getAll(
            int page,
            int size
    );


    long count(Long userId);


    long unread(Long userId);


    void markRead(Long id);


    Map<String, Long> stats();

}