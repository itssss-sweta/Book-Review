package com.example.demo.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    @NotNull(message = "Book review cannot be empty.")
    private String userReview;
}