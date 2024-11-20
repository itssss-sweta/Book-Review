package com.example.demo.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.model.ResponseModel;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseModel<String>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseUtil.unsupportedMediaTypeResponse("Unsupported Media Type for request.");
    }

}