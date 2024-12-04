package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.GenreRepository;
import com.example.demo.utils.ResponseUtil;

import jakarta.validation.Valid;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public BookService(BookRepository bookRepository, GenreRepository genreRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${api.key}")
    private String apiKey;

    public static final int BITE_SIZE = 50 * 1024 * 1024;

    public ResponseEntity<ResponseModel<Book>> postBook(@Valid BookDto bookDto, MultipartFile imageFile,
            List<Long> genreIds) {
        try {
            if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
                return ResponseUtil.conflictResponse("ISBN number already exists.");
            }
            if (imageFile.getBytes().length > BITE_SIZE) {
                return ResponseUtil.badRequestResponse("Image size exceeds the maximum allowed limit.");
            }
            String imageUrl = uploadImageToExternalApi(imageFile);

            if (imageUrl == null) {
                return ResponseUtil.notFoundResponse("Error while uploading image.");
            }
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
            book.setImageUrl(imageUrl);
            List<Genre> genres = genreRepository.findAllById(genreIds);
            if (genres.isEmpty()) {
                return ResponseUtil.notFoundResponse("Invalid genres provided");
            }
            book.setGenres(genres);
            Book savedBook = bookRepository.save(book);
            return ResponseUtil.createdResponse(savedBook, "Book successfully created!");
        } catch (DataIntegrityViolationException e) {
            return ResponseUtil.conflictResponse("ISBN number already exists.");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while creating the book: " + e.getMessage());
        }
    }

    private String uploadImageToExternalApi(MultipartFile imageFile) {
        try {
            byte[] imageData = imageFile.getBytes();

            // Create a multipart request with the image data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Use InputStreamResource to wrap the byte array as an input stream
            ByteArrayResource byteArrayResource = new ByteArrayResource(imageData) {
                @Override
                public String getFilename() {
                    return imageFile.getOriginalFilename();
                }
            };

            body.add("api_key", apiKey);
            body.add("file", byteArrayResource);
            body.add("title", imageFile.getOriginalFilename());

            HttpHeaders headers = new HttpHeaders();

            // Create a request entity with the body and headers
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Create a RestTemplate and send the request
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    "https://api.imghippo.com/v1/upload",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {

                    });

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                var responseBody = responseEntity.getBody();
                if (responseBody != null && responseBody.containsKey("data")) {
                    System.out.println("Data:" + responseBody.get("data"));
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                    if (data != null && data.containsKey("url")) {
                        System.out.println("Url:" + data.get("url"));
                        return (String) data.get("url");
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean deleteImageFromExternalApi(String imageUrl) {
        try {

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            body.add("api_key", apiKey);
            body.add("url", imageUrl);

            HttpHeaders headers = new HttpHeaders();
            System.out.println("Deleting");

            // Create a request entity with the body and headers
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Create a RestTemplate and send the request
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    "https://api.imghippo.com/v1/delete",
                    HttpMethod.DELETE,
                    requestEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {
                    });
            System.out.println("Deleted:" + responseEntity.getStatusCode());

            // Process the response
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                var responseBody = responseEntity.getBody();
                System.out.println(responseBody);
                if (responseBody != null && responseBody.containsKey("deleted_url")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
                Book book = bookWithId.get();
                for (UserModel user : book.getFavoritedByUsers()) {
                    user.getFavouriteBooks().remove(book);
                }
                book.getFavoritedByUsers().clear();

                String imageUrl = book.getImageUrl();
                boolean isImageDeleted = deleteImageFromExternalApi(imageUrl);
                if (isImageDeleted) {
                    bookRepository.deleteById(id);
                    return ResponseUtil.successResponse(null, "Successfully deleted the book.");
                }
                return ResponseUtil.badRequestResponse("An error occured while deleting image.");
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
