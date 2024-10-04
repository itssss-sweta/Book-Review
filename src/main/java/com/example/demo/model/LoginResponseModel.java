package com.example.demo.model;

public class LoginResponseModel {
    private String token;
    private long expiresIn;

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponseModel setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseModel setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

}
