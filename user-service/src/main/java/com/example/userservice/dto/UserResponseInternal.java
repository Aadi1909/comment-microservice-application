package com.example.userservice.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseInternal {

    private String fullName;
    private String username;
    private String email;
}
