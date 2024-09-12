package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
   }


    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> fetchBooks = bookService.getBook();
        return ResponseEntity.ok(fetchBooks);
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book){
        Book postBook = bookService.postBook(book);
        return ResponseEntity.ok(postBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable long id){
      boolean deleteStatus = bookService.deleteBook(id);
      if (deleteStatus) {
            return ResponseEntity.ok("Product with ID " + id + " has been deleted successfully");
        } else {
          return  ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Book with ID " + id + " does not exist."); 
        }
    }
}