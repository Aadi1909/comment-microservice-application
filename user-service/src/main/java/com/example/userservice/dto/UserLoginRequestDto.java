package com.example.userservice.dto;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {

    private String username;
    private String password;
}
