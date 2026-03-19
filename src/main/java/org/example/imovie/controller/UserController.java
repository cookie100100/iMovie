package org.example.imovie.controller;

import org.example.imovie.dto.UserResponse;
import org.example.imovie.entity.User;
import org.example.imovie.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{account}")
    public ResponseEntity<UserResponse> findByAccount(@PathVariable String account) {
        User user = userService.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("User not found with account: " + account));

        UserResponse response = new UserResponse(
                user.getId(),
                user.getAccount(),
                user.getNickName(),
                user.getGender(),
                user.getEmail(),
                user.getPhone()
        );

        return ResponseEntity.ok(response);
    }
}
