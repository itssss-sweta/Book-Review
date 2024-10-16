package com.example.demo.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.model.ResponseModel;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add-favourite/{bookId}")
    public ResponseEntity<ResponseModel<Book>> addFavourite(@RequestHeader("Authorization") String token,
            @PathVariable Long bookId) {
        String jwtToken = token.substring(7);
        return userService.addFavouriteBook(jwtToken, bookId);
    }

    @GetMapping("/get-favourites")
    public ResponseEntity<ResponseModel<Set<Book>>> getFavourites(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        return userService.getFavouriteBooks(jwtToken);
    }

    @DeleteMapping("/remove-favourites/{bookId}")
    public ResponseEntity<ResponseModel<Book>> removeFavourite(@RequestHeader("Authorization") String token,
            @PathVariable Long bookId) {
        String jwtToken = token.substring(7);
        return userService.removeFavouriteBook(jwtToken, bookId);
    }

}
