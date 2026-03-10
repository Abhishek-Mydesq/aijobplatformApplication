package com.aijobplatform.user.auth;

import com.aijobplatform.user.common.ApiResponse;
import com.aijobplatform.user.dto.AuthResponse;
import com.aijobplatform.user.dto.LoginRequest;
import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;
import com.aijobplatform.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse user = userService.registerUser(request);
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(user)
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(response)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        authService.logout(refreshToken);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .data("User logged out successfully")
                .build();
    }

}