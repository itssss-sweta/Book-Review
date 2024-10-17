package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseModel<T> {
    private String message;
    private int statusCode;
    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;

    public ResponseModel(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.success = true;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    public ResponseModel(int statusCode, String error, String message) {
        this.statusCode = statusCode;
        this.success = false;
        this.message = message;
        this.data = null;
        this.error = error;
    }
}
