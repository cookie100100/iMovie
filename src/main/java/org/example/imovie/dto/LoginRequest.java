package org.example.imovie.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "account cannot be blank")
    private String account;

    @NotBlank(message = "password cannot be blank")
    private String password;
}
