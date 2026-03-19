package org.example.imovie.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "account cannot be blank")
    @Length(min = 3, max = 20, message = "account length must be within 3-20 characters")
    private String account;

    @NotBlank(message = "password cannot be blank")
    @Length(min = 6, max = 20, message = "password must be within 6-20 characters")
    private String password;

    @NotEmpty(message = "nickname cannot be empty")
    @Length(min = 2, max = 20, message = "nickname must be within 2-20 characters")
    private String nickName;

    @NotEmpty(message = "gender cannot be empty")
    @Pattern(regexp = "M|F", message = "gender can only be M or F")
    private String gender;

    @Email(message = "not valid email address")
    @Length(min = 10, max = 30, message = "email address between 10-30 characters")
    private String email;

    @Length(max = 15, message = "phone number max 15 characters")
    private String phone;
}
