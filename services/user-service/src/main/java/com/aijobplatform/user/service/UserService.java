package com.aijobplatform.user.service;

import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse registerUser(UserRegisterRequest request);

    UserResponse getUserById(Long id);

    Page<UserResponse> getUsersPage(int page, int size);

    long countUsers();

    boolean exists(Long id);

    UserResponse updateStatus(Long id, Boolean active);

    UserResponse updateProfile(Long id, UserRegisterRequest request);

    void changePassword(Long id, String newPassword);

    boolean existsByEmail(String email);
}