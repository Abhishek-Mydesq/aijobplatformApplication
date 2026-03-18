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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    // ================= REGISTER =================

    @Transactional
    @Override
    public UserResponse registerUser(UserRegisterRequest request) {

        log.info("Register user {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = modelMapper.map(request, User.class);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.USER);

        User savedUser =
                userRepository.saveAndFlush(user);

        return modelMapper.map(
                savedUser,
                UserResponse.class
        );
    }


    // ================= GET =================

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return modelMapper.map(
                user,
                UserResponse.class
        );
    }


    @Override
    public Page<UserResponse> getUsersPage(
            int page,
            int size
    ) {

        Page<User> users =
                userRepository.findAll(
                        PageRequest.of(page, size)
                );

        return users.map(
                u -> modelMapper.map(
                        u,
                        UserResponse.class
                )
        );
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
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    // ================= UPDATE STATUS =================

    @Transactional
    @Override
    public UserResponse updateStatus(
            Long id,
            Boolean active
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        user.setIsActive(active);

        User saved =
                userRepository.saveAndFlush(user);

        return modelMapper.map(
                saved,
                UserResponse.class
        );
    }


    // ================= UPDATE PROFILE =================

    @Transactional
    @Override
    public UserResponse updateProfile(
            Long id,
            UserRegisterRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // email duplicate check
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new ResourceAlreadyExistsException(
                    "Email already exists"
            );
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved =
                userRepository.saveAndFlush(user);

        return modelMapper.map(
                saved,
                UserResponse.class
        );
    }


    // ================= PASSWORD =================

    @Transactional
    @Override
    public void changePassword(
            Long id,
            String newPassword
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.saveAndFlush(user);
    }
    @Override
    public boolean isTokenValid(String email, String token) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRefreshToken() == null) {
            return false;
        }

        return user.getRefreshToken().equals(token);
    }
}