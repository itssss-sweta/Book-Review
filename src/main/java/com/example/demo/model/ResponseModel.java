package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseModel<T> {
    private String message;
    private int status;
    private T data;

    public ResponseModel(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
