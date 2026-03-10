package com.aijobplatform.user.service;
import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRegisterRequest request);
}