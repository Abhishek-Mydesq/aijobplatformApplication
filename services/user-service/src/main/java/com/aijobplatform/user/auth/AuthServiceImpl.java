package com.aijobplatform.user.auth;

import com.aijobplatform.user.dto.AuthResponse;
import com.aijobplatform.user.dto.LoginRequest;
import com.aijobplatform.user.entity.User;
import com.aijobplatform.user.exception.ResourceNotFoundException;
import com.aijobplatform.user.repository.UserRepository;
import com.aijobplatform.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    // ================= LOGIN =================

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new ResourceNotFoundException(
                    "Invalid email or password"
            );
        }

        // ✅ generate tokens
        String accessToken =
                jwtService.generateToken(
                        user.getEmail()
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        user.getEmail()
                );

        // ✅ store ACCESS token in DB (for gateway validation)
        user.setRefreshToken(accessToken);

        userRepository.saveAndFlush(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // ================= REFRESH =================

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        // validate refresh token signature
        jwtService.validateToken(refreshToken);

        String email =
                jwtService.extractEmail(refreshToken);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        // generate new access token
        String newAccessToken =
                jwtService.generateToken(email);

        // store new access token
        user.setRefreshToken(newAccessToken);

        userRepository.saveAndFlush(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }


    // ================= LOGOUT =================

    @Override
    public void logout(String refreshToken) {

        String email =
                jwtService.extractEmail(refreshToken);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        // remove stored token
        user.setRefreshToken(null);

        userRepository.saveAndFlush(user);
    }

}