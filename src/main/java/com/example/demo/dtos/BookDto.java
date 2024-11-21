package com.example.demo.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private String bookTitle;
    private String isbn;
    private String authorName;
    private double price;
    private String imageUrl;
    private double rating;
    private String publisher;
    private int publicationYear;
    private String description;
    private String edition;
    private List<String> genres;
    private int pageCount;
}