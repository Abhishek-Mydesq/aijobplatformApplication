package com.aijobplatform.user.controller;
import com.aijobplatform.user.common.ApiResponse;
import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;
import com.aijobplatform.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @GetMapping("/id/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {

        UserResponse response = userService.getUserById(id);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched")
                .data(response)
                .build();
    }

    //pagination
    @GetMapping("/page")
    public ApiResponse<Page<UserResponse>> getUsersPage(
            @RequestParam int page,
            @RequestParam int size
    ) {

        Page<UserResponse> response =
                userService.getUsersPage(page, size);

        return ApiResponse.<Page<UserResponse>>builder()
                .success(true)
                .message("Users page fetched")
                .data(response)
                .build();
    }
    // count

    @GetMapping("/count")
    public ApiResponse<Long> countUsers() {

        long count = userService.countUsers();

        return ApiResponse.<Long>builder()
                .success(true)
                .message("User count fetched")
                .data(count)
                .build();
    }
    // exists
    @GetMapping("/exists/{id}")
    public ApiResponse<Boolean> exists(@PathVariable Long id) {

        boolean exists = userService.exists(id);

        return ApiResponse.<Boolean>builder()
                .success(true)
                .message("Exists check")
                .data(exists)
                .build();
    }
    // update status

    @PutMapping("/{id}/status")
    public ApiResponse<UserResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Boolean active
    ) {

        UserResponse response =
                userService.updateStatus(id, active);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Status updated")
                .data(response)
                .build();
    }

    // update profile

    @PutMapping("/{id}/profile")
    public ApiResponse<UserResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody UserRegisterRequest request
    ) {

        UserResponse response =
                userService.updateProfile(id, request);

        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Profile updated")
                .data(response)
                .build();
    }

    // change password

    @PutMapping("/{id}/password")
    public ApiResponse<String> changePassword(
            @PathVariable Long id,
            @RequestParam String password
    ) {

        userService.changePassword(id, password);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Password updated")
                .data("OK")
                .build();
    }
    @GetMapping("/exists/email/{email}")
    public ApiResponse<Boolean> existsByEmail(@PathVariable String email) {

        boolean exists = userService.existsByEmail(email);

        return ApiResponse.<Boolean>builder()
                .success(true)
                .message("Email exists check")
                .data(exists)
                .build();
    }
    @GetMapping("/token/validate")
    public Boolean validateToken(
            @RequestParam String email,
            @RequestParam String token
    ) {
        return userService.isTokenValid(email, token);
    }

}