package org.example.imovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private UserResponse user;

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.tokenType = "Bearer";
        this.user = user;
    }
}
