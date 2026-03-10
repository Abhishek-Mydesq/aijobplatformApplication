package com.aijobplatform.user.controller;
import com.aijobplatform.user.common.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> getAllUsers() {
        return ApiResponse.<String>builder()
                .success(true)
                .message("Admin endpoint accessed")
                .data("List of users")
                .build();
    }
}