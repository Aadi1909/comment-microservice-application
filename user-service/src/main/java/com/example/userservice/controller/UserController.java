package com.example.userservice.controller;


import com.example.userservice.dao.UserRepository;
import com.example.userservice.dto.UserResponseInternal;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public UserResponseInternal findUserById(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }
}
