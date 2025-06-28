package com.example.userservice.dto;

import com.example.userservice.model.User;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {

    private String fullName;
    private String username;
    private String email;
    private String password;

    public User to() {
        return User.builder()
                .fullName(this.fullName)
                .username(this.username)
                .email(this.email)
                .password(this.password).build();
    }
}

