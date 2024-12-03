package com.example.demo.model;

public class TokenRefreshResponseModel {
    private String accessToken;

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public TokenRefreshResponseModel setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public TokenRefreshResponseModel setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

}
