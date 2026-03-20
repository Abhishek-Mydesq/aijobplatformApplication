package com.aijobplatform.notification.controller;
import com.aijobplatform.notification.common.PageResponse;
import com.aijobplatform.notification.dto.response.NotificationResponse;
import com.aijobplatform.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/test")
    public NotificationResponse test(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam String type
    ) {

        return service.save(
                userId,
                title,
                message,
                type
        );
    }


    @GetMapping("/user/{userId}")
    public PageResponse<NotificationResponse> getByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return service.getByUser(
                userId,
                page,
                size
        );
    }


    @GetMapping("/unread/page/{userId}")
    public PageResponse<NotificationResponse> getUnread(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return service.getUnread(
                userId,
                page,
                size
        );
    }


    @GetMapping("/all")
    public PageResponse<NotificationResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return service.getAll(
                page,
                size
        );
    }


    @GetMapping("/count/{userId}")
    public long count(
            @PathVariable Long userId
    ) {

        return service.count(userId);
    }


    @GetMapping("/unread/{userId}")
    public long unread(
            @PathVariable Long userId
    ) {

        return service.unread(userId);
    }


    @PutMapping("/read/{id}")
    public void markRead(
            @PathVariable Long id
    ) {

        service.markRead(id);
    }


    @GetMapping("/stats")
    public Map<String, Long> stats() {

        return service.stats();
    }

}