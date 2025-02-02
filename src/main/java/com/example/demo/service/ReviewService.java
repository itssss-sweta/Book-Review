package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.ReviewDto;
import com.example.demo.model.Book;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.Review;
import com.example.demo.model.UserModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ResponseUtil;

@Service
public class ReviewService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JwtService jwtService;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseModel<Review>> reviewBook(long bookId, ReviewDto reviewDto, String token) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                return ResponseUtil.unauthorizedResponse("Invalid or expired token.");
            }
            UserModel user = userRepository.findByEmail(username)
                    .orElse(null);
            if (user == null) {
                return ResponseUtil.notFoundResponse("User not found with email: " +
                        username);
            }
            Book book = bookRepository.findById(bookId)
                    .orElse(null);
            if (book == null) {
                return ResponseUtil.notFoundResponse(("Book not found with ID: " + bookId));
            }
            Review review = new Review();
            review.setUser(user);
            review.setBook(book);
            review.setUserReview(reviewDto.getUserReview());

            reviewRepository.save(review);
            return ResponseUtil.successResponse(null, "Review successfully added for " + book.getBookTitle());

        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An unexpected error occurred: " +
                    e.getMessage());
        }
    }
}
