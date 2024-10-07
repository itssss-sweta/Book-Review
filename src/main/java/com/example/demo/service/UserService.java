package com.example.demo.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.Book;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ResponseUtil;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<ResponseModel<Book>> addFavouriteBook(String token, Long bookId) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                return ResponseUtil.unauthorizedResponse("Invalid or expired token.");
            }
            UserModel user = userRepository.findByEmail(username)
                    .orElse(null);
            if (user == null) {
                return ResponseUtil.notFoundResponse("User not found with email: " + username);
            }
            Book book = bookRepository.findById(bookId)
                    .orElse(null);
            if (book == null) {
                return ResponseUtil.notFoundResponse(("Book not found with ID: " + bookId));
            }
            if (user.getFavouriteBooks().contains(book)) {
                return ResponseUtil.badRequestResponse("Book is already in the user's favorites.");
            }
            user.addFavoriteBook(book);
            userRepository.save(user);
            return ResponseUtil.successResponse(null, "Book added to favorites.");

        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An unexpected error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<Set<Book>>> getFavouriteBooks(String token) {
        String username = jwtService.extractUsername(token);
        UserModel user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        Set<Book> favouriteBooks = user.getFavouriteBooks();

        return ResponseUtil.successResponse(favouriteBooks, "Favorite books retrieved successfully.");
    }
}
