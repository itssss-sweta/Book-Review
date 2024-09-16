package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.handler.ResponseHandler;
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
    public ResponseEntity<Object> getAllBooks(){
        List<Book> fetchBooks = bookService.getBook();
        return ResponseHandler.generateResponse("Successfully Retrieved!", HttpStatus.OK, fetchBooks);
    }

    @PostMapping
    public ResponseEntity<Object> createBook(@RequestBody Book book){
        Book postBook = bookService.postBook(book);
        return ResponseHandler.generateResponse("Successfully Posted!", HttpStatus.CREATED, postBook);

    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable long id, @RequestBody Book book){
        Book updateBook = bookService.updateBook(id,book);
        return ResponseHandler.generateResponse("Successfully Updated!", HttpStatus.CREATED, updateBook);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable long id){
      boolean deleteStatus = bookService.deleteBook(id);
      if (deleteStatus) {
        String successMessage = "Product with ID " +  id +" has been deleted successfully";
        return ResponseHandler.generateResponse(successMessage, HttpStatus.OK, null);
        
    } else {
        String errorMessage = "Book with ID " + id + " does not exist.";
        return ResponseHandler.generateResponse(errorMessage, HttpStatus.NOT_FOUND, null);
        }
    }
}