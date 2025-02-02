package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.dtos.GenreDto;
import com.example.demo.dtos.ReviewDto;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.Review;
import com.example.demo.service.BookService;
import com.example.demo.service.GenreService;
import com.example.demo.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookService bookService;
  @Autowired
  private GenreService genreService;
  @Autowired
  private ReviewService reviewService;

  @GetMapping
  public ResponseEntity<ResponseModel<List<Book>>> getAllBooks() {
    return bookService.getBook();
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<ResponseModel<Book>> createBook(@Valid @RequestPart("book") BookDto book,
      @RequestPart("imageFile") MultipartFile imageFile, @RequestParam("genreIds") List<Long> genreIds) {
    return bookService.postBook(book, imageFile, genreIds);
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<ResponseModel<Book>> updateBook(@PathVariable long id, @RequestBody BookDto book) {
    return bookService.updateBook(id, book);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseModel<Book>> deleteBook(@PathVariable long id) {
    return bookService.deleteBook(id);
  }

  @PostMapping("/genres")
  public ResponseEntity<ResponseModel<Genre>> addGenre(@RequestBody GenreDto genre) {
    return genreService.createGenre(genre);
  }

  @GetMapping("/genres")
  public ResponseEntity<ResponseModel<List<Genre>>> getAllGenres() {
    return genreService.getAllGenres();

  }

  @GetMapping("/genres/{genreId}")
  public ResponseEntity<ResponseModel<Genre>> getGenreById(@PathVariable Long genreId) {
    return genreService.getGenreById(genreId);
  }

  @PutMapping("/genres/{genreId}")
  public ResponseEntity<ResponseModel<Genre>> updateGenreById(@PathVariable Long genreId, @RequestBody GenreDto genre) {
    return genreService.updateGenreById(genreId, genre);
  }

  @DeleteMapping("/genres/{genreId}")
  public ResponseEntity<ResponseModel<Genre>> deleteGenreById(@PathVariable Long genreId) {
    return genreService.deleteGenreById(genreId);
  }

  @PostMapping("/review-book/{bookId}")
  public ResponseEntity<ResponseModel<Review>> reviewBook(@RequestHeader("Authorization") String token,
      @PathVariable Long bookId, @RequestBody ReviewDto reviewDto) {
    String jwtToken = token.substring(7);
    return reviewService.reviewBook(bookId, reviewDto, jwtToken);
  }
}