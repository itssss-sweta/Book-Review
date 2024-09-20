package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseModel<UserModel>> signUpUser(@Valid @RequestBody UserModel newUser) {
        return userService.signUp(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel<UserModel>> login(@Valid @RequestBody String email,
            @RequestBody String password) {
        return userService.login(email, password);
    }
}
