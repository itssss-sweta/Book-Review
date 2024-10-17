package com.example.demo.model;

public class LoginResponseModel {
    private String token;
    private long expiresIn;
    private UserModel userModel;

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public UserModel getUser() {
        return userModel;
    }

    public LoginResponseModel setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseModel setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public LoginResponseModel setUserModel(UserModel userModel) {
        this.userModel = userModel;
        return this;
    }

}
