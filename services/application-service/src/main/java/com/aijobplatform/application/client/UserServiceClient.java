package com.aijobplatform.application.client;

import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long id);

}