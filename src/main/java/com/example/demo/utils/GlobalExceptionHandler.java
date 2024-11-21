package com.example.demo.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.model.ResponseModel;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ResponseModel<String>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseUtil.unsupportedMediaTypeResponse("Unsupported Media Type for request.");
    }

    // Handle MethodArgumentNotValidException for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel<String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessages.add(error.getDefaultMessage());
        });
        return ResponseUtil.badRequestResponse(String.join(", ", errorMessages));
    }

    // Handle ConstraintViolationException (if any constraint violations)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseModel<String>> handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> errorMessages = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> {
            errorMessages.add(violation.getMessage());
        });
        return ResponseUtil.badRequestResponse(String.join(", ", errorMessages));
    }

    // Catch other general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<String>> handleGlobalException(Exception ex, WebRequest request) {
        return ResponseUtil.serverErrorResponse("An unexpected error occurred: " + ex.getMessage());
    }

}