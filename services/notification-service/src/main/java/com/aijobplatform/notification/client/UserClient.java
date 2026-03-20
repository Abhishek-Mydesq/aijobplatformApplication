package com.aijobplatform.notification.client;
import com.aijobplatform.notification.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        path = "/api/users"
)
public interface UserClient {

    @GetMapping("/exists/{id}")
    ApiResponse<Boolean> exists(
            @PathVariable Long id
    );

}