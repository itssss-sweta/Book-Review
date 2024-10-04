package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseModel<T> {
    private String message;
    private int statusCode;
    private Boolean status;
    private T data;

    public ResponseModel(int statusCode, String message, T data, Boolean status) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
