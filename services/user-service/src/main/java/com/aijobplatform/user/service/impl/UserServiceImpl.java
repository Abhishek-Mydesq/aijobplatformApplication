package com.aijobplatform.user.service.impl;

import com.aijobplatform.user.dto.UserRegisterRequest;
import com.aijobplatform.user.dto.UserResponse;
import com.aijobplatform.user.entity.Role;
import com.aijobplatform.user.entity.User;
import com.aijobplatform.user.exception.ResourceAlreadyExistsException;
import com.aijobplatform.user.exception.ResourceNotFoundException;
import com.aijobplatform.user.repository.UserRepository;
import com.aijobplatform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponse.class);
    }
    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return modelMapper.map(user, UserResponse.class);
    }
    @Override
    public Page<UserResponse> getUsersPage(int page, int size) {

        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        return users.map(user -> modelMapper.map(user, UserResponse.class));
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public boolean exists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserResponse updateStatus(Long id, Boolean active) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(active);

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public UserResponse updateProfile(Long id, UserRegisterRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public void changePassword(Long id, String newPassword) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}