package org.example.imovie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.imovie.dto.AuthResponse;
import org.example.imovie.dto.LoginRequest;
import org.example.imovie.dto.RegisterRequest;
import org.example.imovie.dto.UserResponse;
import org.example.imovie.exception.GlobalExceptionHandler;
import org.example.imovie.service.impl.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    private UserResponse mockUserResponse() {
        return new UserResponse(1L, "testuser", "TestUser", "M", "test@example.com", "1234567890");
    }

    private AuthResponse mockAuthResponse() {
        return new AuthResponse("jwt-token", mockUserResponse());
    }

    @Test
    void register_validRequest_shouldReturn200WithUserInfo() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setAccount("testuser");
        request.setPassword("password123");
        request.setNickName("TestUser");
        request.setGender("M");
        request.setEmail("test@example.com");
        request.setPhone("1234567890");

        when(authService.register(any())).thenReturn(mockUserResponse());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.account").value("testuser"))
                .andExpect(jsonPath("$.data.nickName").value("TestUser"))
                .andExpect(jsonPath("$.data.token").doesNotExist());
    }

    @Test
    void register_accountTooShort_shouldReturn400() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setAccount("ab");           // too short (min 3)
        request.setPassword("password123");
        request.setNickName("TestUser");
        request.setGender("M");
        request.setEmail("test@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void register_invalidGender_shouldReturn400() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setAccount("testuser");
        request.setPassword("password123");
        request.setNickName("TestUser");
        request.setGender("X");             // invalid (must be M or F)
        request.setEmail("test@example.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void register_duplicateAccount_shouldReturn400() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setAccount("testuser");
        request.setPassword("password123");
        request.setNickName("TestUser");
        request.setGender("M");
        request.setEmail("test@example.com");

        when(authService.register(any()))
                .thenThrow(new RuntimeException("Account already exists: testuser"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Account already exists: testuser"));
    }

    @Test
    void login_validRequest_shouldReturn200WithToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("testuser");
        request.setPassword("password123");

        when(authService.login(any())).thenReturn(mockAuthResponse());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.user.account").value("testuser"));
    }

    @Test
    void login_invalidPassword_shouldReturn401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("testuser");
        request.setPassword("wrongpassword");

        when(authService.login(any())).thenThrow(new RuntimeException("Invalid password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("Invalid password"));
    }

    @Test
    void login_userNotFound_shouldReturn401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setAccount("unknown");
        request.setPassword("password123");

        when(authService.login(any())).thenThrow(new RuntimeException("User not found with account: unknown"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void login_blankFields_shouldReturn400() throws Exception {
        LoginRequest request = new LoginRequest();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
