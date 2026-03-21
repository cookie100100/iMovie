package org.example.imovie.service.impl;

import org.example.imovie.dto.*;
import org.example.imovie.entity.User;
import org.example.imovie.exception.BusinessException;
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

    public UserResponse register(RegisterRequest request) {
        // Check if account already exists
        if (userService.findByAccount(request.getAccount()).isPresent()) {
            throw new BusinessException(RespCode.ACCOUNT_ALREADY_EXISTS);
        }

        // Map DTO to entity
        User user = new User();
        user.setAccount(request.getAccount());
        
        // Generate salt and hash password
        String salt = java.util.UUID.randomUUID().toString().substring(0, 8);
        user.setSalt(salt);
        user.setPassword(org.springframework.util.DigestUtils.md5DigestAsHex((request.getPassword() + salt).getBytes()));
        
        user.setNickName(request.getNickName());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRegisterTime(new java.util.Date());

        // Save user
        User savedUser = userService.saveUser(user);

        return toUserResponse(savedUser);
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
