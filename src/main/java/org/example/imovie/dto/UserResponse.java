package org.example.imovie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String account;
    private String nickName;
    private String gender;
    private String email;
    private String phone;
}
