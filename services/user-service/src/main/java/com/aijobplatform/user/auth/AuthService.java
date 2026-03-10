package com.aijobplatform.user.auth;
import com.aijobplatform.user.dto.AuthResponse;
import com.aijobplatform.user.dto.LoginRequest;
public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
}