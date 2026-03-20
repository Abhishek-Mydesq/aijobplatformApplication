package com.aijobplatform.notification.impl;
import com.aijobplatform.notification.client.UserClient;
import com.aijobplatform.notification.common.NotificationMapper;
import com.aijobplatform.notification.common.PageResponse;
import com.aijobplatform.notification.dto.ApiResponse;
import com.aijobplatform.notification.dto.response.NotificationResponse;
import com.aijobplatform.notification.entity.Notification;
import com.aijobplatform.notification.entity.enums.NotificationStatus;
import com.aijobplatform.notification.entity.enums.NotificationType;
import com.aijobplatform.notification.exception.ResourceNotFoundException;
import com.aijobplatform.notification.kafka.dto.NotificationEvent;
import com.aijobplatform.notification.repository.NotificationRepository;
import com.aijobplatform.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final UserClient userClient;


    // ✅ REQUIRED FOR KAFKA / SCHEDULER / INTERNAL
    @Override
    public Notification save(Notification notification) {

        log.info(
                "Saving notification user={} type={} source={}",
                notification.getUserId(),
                notification.getType(),
                notification.getSourceService()
        );

        return repository.save(notification);
    }


    @Override
    public NotificationResponse save(
            Long userId,
            String title,
            String message,
            String type
    ) {

        ApiResponse<Boolean> response =
                userClient.exists(userId);

        if (response == null ||
                response.getData() == null ||
                !response.getData()) {

            throw new ResourceNotFoundException(
                    "User not found " + userId
            );
        }

        NotificationType notificationType;

        try {
            notificationType =
                    NotificationType.valueOf(type);
        } catch (Exception e) {
            notificationType = NotificationType.SYSTEM;
        }

        Notification notification =
                Notification.builder()
                        .userId(userId)
                        .title(title)
                        .message(message)
                        .type(notificationType)
                        .priority(null)
                        .sourceService("API")
                        .metadata(null)
                        .status(NotificationStatus.CREATED)
                        .isRead(false)
                        .build();

        Notification saved =
                repository.save(notification);

        return NotificationMapper.toResponse(saved);
    }


    @Override
    public List<NotificationResponse> saveBulk(
            List<NotificationEvent> events
    ) {

        List<Notification> list = new ArrayList<>();

        for (NotificationEvent e : events) {

            NotificationType type =
                    e.getType() != null
                            ? e.getType()
                            : NotificationType.SYSTEM;

            list.add(
                    Notification.builder()
                            .userId(e.getUserId())
                            .title(e.getTitle())
                            .message(e.getMessage())
                            .type(type)
                            .priority(e.getPriority())
                            .sourceService(e.getSourceService())
                            .metadata(e.getMetadata())
                            .status(NotificationStatus.CREATED)
                            .isRead(false)
                            .build()
            );
        }

        repository.saveAll(list);

        return list.stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }


    @Override
    public PageResponse<NotificationResponse> getByUser(
            Long userId,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Notification> data =
                repository.findByUserId(
                        userId,
                        pageable
                );

        return PageResponse.of(
                data.map(
                        NotificationMapper::toResponse
                )
        );
    }


    @Override
    public PageResponse<NotificationResponse> getUnread(
            Long userId,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Notification> data =
                repository.findByUserIdAndIsReadFalse(
                        userId,
                        pageable
                );

        return PageResponse.of(
                data.map(
                        NotificationMapper::toResponse
                )
        );
    }


    @Override
    public PageResponse<NotificationResponse> getAll(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Notification> data =
                repository.findAll(pageable);

        return PageResponse.of(
                data.map(
                        NotificationMapper::toResponse
                )
        );
    }


    @Override
    public long count(Long userId) {
        return repository.countByUserId(userId);
    }


    @Override
    public long unread(Long userId) {
        return repository
                .countByUserIdAndIsReadFalse(userId);
    }


    @Override
    public void markRead(Long id) {

        Notification n =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found"
                                ));

        n.setIsRead(true);
        n.setStatus(NotificationStatus.READ);
        n.setReadAt(LocalDateTime.now());

        repository.save(n);
    }


    @Override
    public Map<String, Long> stats() {

        Map<String, Long> map = new HashMap<>();

        map.put("TOTAL", repository.count());

        map.put(
                "CREATED",
                repository.countByStatus(
                        NotificationStatus.CREATED
                )
        );

        map.put(
                "READ",
                repository.countByStatus(
                        NotificationStatus.READ
                )
        );

        map.put(
                "FAILED",
                repository.countByStatus(
                        NotificationStatus.FAILED
                )
        );

        return map;
    }
}