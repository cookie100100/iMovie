package org.example.imovie.service.impl;

import org.example.imovie.dto.*;
import org.example.imovie.entity.User;
import org.example.imovie.security.JwtUtil;
import org.example.imovie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if account already exists
        if (userService.findByAccount(request.getAccount()).isPresent()) {
            throw new RuntimeException("Account already exists: " + request.getAccount());
        }

        // Map DTO to entity
        User user = new User();
        user.setAccount(request.getAccount());
        user.setPassword(request.getPassword());
        user.setNickName(request.getNickName());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // Save user (password will be hashed in UserServiceImpl)
        User savedUser = userService.saveUser(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(savedUser.getAccount());

        // Build response
        UserResponse userResponse = toUserResponse(savedUser);
        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest request) {
        // Validate credentials
        User user = userService.validateUser(request.getAccount(), request.getPassword());

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getAccount());

        // Build response
        UserResponse userResponse = toUserResponse(user);
        return new AuthResponse(token, userResponse);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getAccount(),
                user.getNickName(),
                user.getGender(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
