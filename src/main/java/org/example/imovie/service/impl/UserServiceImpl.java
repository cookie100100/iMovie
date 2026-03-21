package org.example.imovie.service.impl;

import org.example.imovie.dto.RespCode;
import org.example.imovie.entity.User;
import org.example.imovie.exception.BusinessException;
import org.example.imovie.repository.UserRepository;
import org.example.imovie.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        // Password already hashed in AuthService for new users
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public User validateUser(String account, String password) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new BusinessException(RespCode.USER_NOT_FOUND));

        // MD5(password + salt)
        String hashedInput = org.springframework.util.DigestUtils.md5DigestAsHex((password + user.getSalt()).getBytes());
        if (!hashedInput.equals(user.getPassword())) {
            throw new BusinessException(RespCode.INVALID_PASSWORD);
        }

        return user;
    }
}
