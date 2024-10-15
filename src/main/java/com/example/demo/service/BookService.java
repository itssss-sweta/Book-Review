package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.ResponseModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.utils.ResponseUtil;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<ResponseModel<Book>> postBook(BookDto bookDto) {
        try {
            Book book = new Book();
            book.setBookTitle(bookDto.getBookTitle());
            book.setIsbn(bookDto.getIsbn());
            book.setAuthorName(bookDto.getAuthorName());
            book.setPrice(bookDto.getPrice());
            if (book.getAuthorName() == null || book.getAuthorName().isEmpty()) {
                return ResponseUtil.badRequestResponse("Author name cannot be empty.");
            }
            if (book.getBookTitle() == null || book.getBookTitle().isEmpty()) {
                return ResponseUtil.badRequestResponse("Book title cannot be empty.");
            }
            if (book.getPrice() <= 0) {
                return ResponseUtil.badRequestResponse("Price must be greater than zero.");
            }
            if (book.getIsbn() <= 9 || book.getIsbn() > 10) {
                return ResponseUtil.badRequestResponse("Isbn number should be 10 digits.");
            }

            Book savedBook = bookRepository.save(book);
            return ResponseUtil.createdResponse(savedBook, "Book successfully created!");
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.conflictResponse("ISBN Number already exists");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while creating the book: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<List<Book>>> getBook() {
        try {
            List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                return ResponseUtil.noContentResponse("No books found.");
            }

            return ResponseUtil.successResponse(books, "Books fetched successfully.");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while fetching books: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<Book>> deleteBook(long id) {
        try {
            if (id <= 0) {
                return ResponseUtil.badRequestResponse("Invalid book ID provided.");
            }

            Optional<Book> bookWithId = bookRepository.findById(id);

            if (bookWithId.isPresent()) {
                bookRepository.deleteById(id);
                return ResponseUtil.successResponse(null, "Successfully deleted the book.");
            } else {
                return ResponseUtil.notFoundResponse("Book not found with ID " + id);
            }
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while deleting the book: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<Book>> updateBook(long id, BookDto newBook) {
        try {
            if (id <= 0) {
                return ResponseUtil.badRequestResponse("Invalid book ID provided.");
            }

            Optional<Book> bookWithId = bookRepository.findById(id);

            if (bookWithId.isPresent()) {
                Book book = bookWithId.get();

                if (newBook.getAuthorName() == null || newBook.getAuthorName().isEmpty()) {
                    return ResponseUtil.badRequestResponse("Author name cannot be empty.");
                }
                if (newBook.getBookTitle() == null || newBook.getBookTitle().isEmpty()) {
                    return ResponseUtil.badRequestResponse("Book title cannot be empty.");
                }
                // TODO: donot let isbn number change
                if (newBook.getIsbn() <= 9 && newBook.getIsbn() > 10) {
                    return ResponseUtil.badRequestResponse("Isbn number should be 10 digits.");
                }
                book.setAuthorName(newBook.getAuthorName());
                book.setIsbn(newBook.getIsbn());
                book.setBookTitle(newBook.getBookTitle());
                book.setPrice(newBook.getPrice());

                Book updatedBook = bookRepository.save(book);
                return ResponseUtil.successResponse(updatedBook, "Successfully Updated!");

            } else {
                return ResponseUtil.notFoundResponse("Book not found with ID " + id);
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.conflictResponse("ISBN Number already exists");
        } catch (IllegalArgumentException e) {
            return ResponseUtil.badRequestResponse("Invalid data provided: " + e.getMessage());
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while updating the book: " + e.getMessage());
        }
    }
}
