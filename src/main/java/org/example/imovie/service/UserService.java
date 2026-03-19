package org.example.imovie.service;

import org.example.imovie.entity.User;

import java.util.Optional;

public interface UserService {
    public User saveUser(User user);
    public Optional<User> findByAccount(String account);
    public User validateUser(String account, String password);
}
