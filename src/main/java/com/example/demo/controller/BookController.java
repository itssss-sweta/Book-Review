package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private List<Book> books = new ArrayList<>();

    @GetMapping
    public List<Book> getAllBooks(){
        return books;
    }

    @PostMapping
    public String createBook(@RequestBody Book book){
        books.add(book);
        return "Book Inserted: " + book.getBookTitle();
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable long id){
        for(Book book : books){
            if(book.getId() == id){
                books.remove(book);
                return book.getBookTitle() + "with Book Id" + id +"has been deleted.";
            }
        }
        return "Book with ID " + id + " not found.";
    }

}