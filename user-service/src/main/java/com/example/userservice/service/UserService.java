package com.example.userservice.service;


import com.example.userservice.dao.UserRepository;
import com.example.userservice.dto.UserResponseInternal;
import com.example.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseInternal getUser(Integer Id) {
        Optional<User> user = userRepository.findById(Id);
        return user.map(value -> UserResponseInternal
                .builder()
                .fullName(value.getFullName())
                .email(value.getEmail())
                .username(value.getUsername())
                .build()).orElse(null);
    }
}
