package com.aijobplatform.notification.service;
import com.aijobplatform.notification.common.PageResponse;
import com.aijobplatform.notification.dto.response.NotificationResponse;
import com.aijobplatform.notification.kafka.dto.NotificationEvent;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    NotificationResponse save(
            Long userId,
            String title,
            String message,
            String type
    );

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