package org.example.imovie.service;

import org.example.imovie.entity.User;
import org.example.imovie.repository.UserRepository;
import org.example.imovie.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setAccount("testuser");
        user.setPassword("plainPassword");
        user.setNickName("TestUser");
        user.setGender("M");
        user.setEmail("test@example.com");
    }

    @Test
    void saveUser_shouldEncodePasswordBeforeSaving() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userServiceImpl.saveUser(user);

        assertThat(saved.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void saveUser_shouldReturnSavedUser() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userServiceImpl.saveUser(user);

        assertThat(saved).isNotNull();
        assertThat(saved.getAccount()).isEqualTo("testuser");
    }

    @Test
    void findByAccount_userExists_shouldReturnUser() {
        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userServiceImpl.findByAccount("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getAccount()).isEqualTo("testuser");
    }

    @Test
    void findByAccount_userNotExists_shouldReturnEmpty() {
        when(userRepository.findByAccount("unknown")).thenReturn(Optional.empty());

        Optional<User> result = userServiceImpl.findByAccount("unknown");

        assertThat(result).isEmpty();
    }

    @Test
    void validateUser_correctCredentials_shouldReturnUser() {
        user.setPassword("hashedPassword");
        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);

        User result = userServiceImpl.validateUser("testuser", "plainPassword");

        assertThat(result.getAccount()).isEqualTo("testuser");
    }

    @Test
    void validateUser_userNotFound_shouldThrow() {
        when(userRepository.findByAccount("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.validateUser("unknown", "any"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found with account: unknown");
    }

    @Test
    void validateUser_wrongPassword_shouldThrow() {
        user.setPassword("hashedPassword");
        when(userRepository.findByAccount("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThatThrownBy(() -> userServiceImpl.validateUser("testuser", "wrongPassword"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid password");
    }
}
