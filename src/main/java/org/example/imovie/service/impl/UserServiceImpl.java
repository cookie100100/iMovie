package org.example.imovie.service.impl;

import org.example.imovie.entity.User;
import org.example.imovie.repository.UserRepository;
import org.example.imovie.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public User validateUser(String account, String password) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("User not found with account: " + account));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}
