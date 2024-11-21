package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.model.ImageModel;
import com.example.demo.model.ResponseModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.utils.ImageUtils;
import com.example.demo.utils.ResponseUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final ImageRepository imageRepository;
    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, ImageRepository imageRepository,
            GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional
    public ResponseEntity<ResponseModel<Book>> postBook(@Valid BookDto bookDto, MultipartFile imageFile,
            List<Long> genreIds) {
        try {
            if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
                return ResponseUtil.conflictResponse("ISBN number already exists.");
            }
            if (imageFile.getBytes().length > ImageUtils.BITE_SIZE) {
                return ResponseUtil.badRequestResponse("Image size exceeds the maximum allowed limit.");
            }
            byte[] compressedImage = ImageUtils.compressImage(imageFile.getBytes());
            var imageToSave = ImageModel.builder()
                    .imageName(imageFile.getOriginalFilename())
                    .imageBytes(compressedImage)
                    .build();
            Book book = new Book();
            book.setBookTitle(bookDto.getBookTitle());
            book.setIsbn(bookDto.getIsbn());
            book.setAuthorName(bookDto.getAuthorName());
            book.setPrice(bookDto.getPrice());
            book.setDescription(bookDto.getDescription());
            book.setEdition(bookDto.getEdition());
            book.setPageCount(bookDto.getPageCount());
            book.setPublicationYear(bookDto.getPublicationYear());
            book.setPublisher(bookDto.getPublisher());
            book.setRating(bookDto.getRating());
            List<Genre> genres = genreRepository.findAllById(genreIds);
            if (genres.isEmpty()) {
                return ResponseUtil.notFoundResponse("Invalid genres provided");
            }
            book.setGenres(genres);
            ImageModel savedImage = imageRepository.save(imageToSave);
            String imageUrl = "/images/" + savedImage.getImageId();
            book.setImageUrl(imageUrl);
            Book savedBook = bookRepository.save(book);
            return ResponseUtil.createdResponse(savedBook, "Book successfully created!");
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.conflictResponse("ISBN number already exists.");
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
                if (String.valueOf(book.getIsbn()).length() != 10) {
                    System.out.println("Invalid ISBN: " + book.getIsbn());
                    return ResponseUtil.badRequestResponse("ISBN number should be exactly 10 digits.");
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
