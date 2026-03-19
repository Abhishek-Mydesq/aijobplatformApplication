package com.aijobplatform.resume.client;
import com.aijobplatform.resume.common.ApiResponse;
import com.aijobplatform.resume.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/id/{id}")
    ApiResponse<UserResponse> getUserById(@PathVariable Long id);

}