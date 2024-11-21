package com.example.demo.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    @NotNull(message = "Book title cannot be empty.")
    private String bookTitle;

    @Size(min = 10, max = 10, message = "ISBN number should be exactly 10 digits.")
    private String isbn;

    @NotNull(message = "Author name cannot be empty.")
    private String authorName;

    @Positive(message = "Price must be greater than zero.")
    private double price;

    @DecimalMin(value = "0.0", inclusive = false, message = "Rating should be between 0 and 5.")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating should be between 0 and 5.")
    private double rating;

    @NotNull(message = "Publisher cannot be empty.")
    private String publisher;

    @Min(value = 1500, message = "Invalid year.")
    private int publicationYear;

    @Size(max = 1000, message = "Description cannot be longer than 1000 characters.")
    private String description;

    @NotNull(message = "Edition cannot be empty.")
    private String edition;

    @Min(value = 1, message = "Page Count must be positive.")
    private int pageCount;
}