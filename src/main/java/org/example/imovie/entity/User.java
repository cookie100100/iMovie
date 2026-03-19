package org.example.imovie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "account cannot be blank")
    @Length(min = 3, max = 20, message = "account length must be within 3-20 characters")
    @Column(length = 20, unique = true)
    private String account;

    @NotBlank(message = "password cannot be blank")
    @Column(length = 100)
    private String password;

    @NotEmpty(message = "nickname cannot be empty")
    @Length(min = 2, max = 20, message = "nickname must be within 2-20 characters")
    private String nickName;

    @NotEmpty(message = "gender cannot be empty")
    @Pattern(regexp = "M|F", message = "gender can only be M or F")
    @Column(length = 1)
    private String gender;

    @Email(message = "not valid email address")
    @Length(min = 10, max = 30, message = "email address between 10-30 characters")
    @Column(length = 40)
    private String email;

    @Column(length = 15)
    private String phone;
}
