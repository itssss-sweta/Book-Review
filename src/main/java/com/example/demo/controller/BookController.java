package com.example.demo.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.ResponseModel;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

  final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public ResponseEntity<ResponseModel<List<Book>>> getAllBooks() {
    return bookService.getBook();
  }

  @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  public ResponseEntity<ResponseModel<Book>> createBook(@RequestPart("book") BookDto book,
      @RequestPart("imageFile") MultipartFile imageFile) {
    System.out.println("POSTING");
    if (imageFile != null)
      System.out.println(imageFile);
    System.out.println(book);
    return bookService.postBook(book, imageFile);
  }

  @PutMapping("/edit/{id}")
  public ResponseEntity<ResponseModel<Book>> updateBook(@PathVariable long id, @RequestBody BookDto book) {
    return bookService.updateBook(id, book);

  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseModel<Book>> deleteBook(@PathVariable long id) {
    return bookService.deleteBook(id);
  }
}