package com.aijobplatform.user.controller;
import com.aijobplatform.user.common.ApiResponse;
import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;
import com.aijobplatform.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<String> getProfile() {

        return ApiResponse.<String>builder()
                .success(true)
                .message("Access granted to protected endpoint")
                .data("User profile data")
                .build();
    }
}