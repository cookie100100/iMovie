package org.example.imovie.service;

import org.example.imovie.dto.AuthResponse;
import org.example.imovie.dto.LoginRequest;
import org.example.imovie.dto.RegisterRequest;
import org.example.imovie.entity.User;
import org.example.imovie.security.JwtUtil;
import org.example.imovie.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setAccount("testuser");
        savedUser.setPassword("hashedPassword");
        savedUser.setNickName("TestUser");
        savedUser.setGender("M");
        savedUser.setEmail("test@example.com");
        savedUser.setPhone("1234567890");
    }

    private RegisterRequest buildRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setAccount("testuser");
        request.setPassword("password123");
        request.setNickName("TestUser");
        request.setGender("M");
        request.setEmail("test@example.com");
        request.setPhone("1234567890");
        return request;
    }

    @Test
    void register_success_shouldReturnAuthResponse() {
        when(userService.findByAccount("testuser")).thenReturn(Optional.empty());
        when(userService.saveUser(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken("testuser")).thenReturn("jwt-token");

        AuthResponse response = authService.register(buildRegisterRequest());

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser().getAccount()).isEqualTo("testuser");
        assertThat(response.getUser().getNickName()).isEqualTo("TestUser");
        assertThat(response.getUser().getId()).isEqualTo(1L);
    }

    @Test
    void register_accountAlreadyExists_shouldThrow() {
        when(userService.findByAccount("testuser")).thenReturn(Optional.of(savedUser));

        assertThatThrownBy(() -> authService.register(buildRegisterRequest()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account already exists");

        verify(userService, never()).saveUser(any());
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void login_success_shouldReturnAuthResponse() {
        LoginRequest request = new LoginRequest();
        request.setAccount("testuser");
        request.setPassword("password123");

        when(userService.validateUser("testuser", "password123")).thenReturn(savedUser);
        when(jwtUtil.generateToken("testuser")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getUser().getAccount()).isEqualTo("testuser");
    }

    @Test
    void login_invalidPassword_shouldThrow() {
        LoginRequest request = new LoginRequest();
        request.setAccount("testuser");
        request.setPassword("wrongpassword");

        when(userService.validateUser("testuser", "wrongpassword"))
                .thenThrow(new RuntimeException("Invalid password"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");
    }

    @Test
    void login_userNotFound_shouldThrow() {
        LoginRequest request = new LoginRequest();
        request.setAccount("unknown");
        request.setPassword("password123");

        when(userService.validateUser("unknown", "password123"))
                .thenThrow(new RuntimeException("User not found with account: unknown"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
