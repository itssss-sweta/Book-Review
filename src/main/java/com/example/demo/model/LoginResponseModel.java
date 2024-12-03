package com.example.demo.model;

import com.example.demo.dtos.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class LoginResponseModel {
    private String token;

    private String refreshToken;

    @JsonIgnore
    private long expiresIn;

    private UserDto userModel;

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public UserDto getDetail() {
        return userModel;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponseModel setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseModel setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public LoginResponseModel setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public LoginResponseModel setUserModel(UserDto userModel) {
        this.userModel = userModel;
        return this;
    }

}
