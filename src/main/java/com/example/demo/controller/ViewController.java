package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.dtos.GenreDto;
import com.example.demo.dtos.LoginDto;
import com.example.demo.model.Book;
import com.example.demo.model.Genre;
import com.example.demo.model.LoginResponseModel;
import com.example.demo.model.ResponseModel;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.BookService;
import com.example.demo.service.GenreService;
import com.example.demo.utils.ResponseUtil;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ViewController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private BookService bookService;

    @GetMapping("/login/admin")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    @PostMapping("/login/admin")
    public String loginUser(@RequestParam("email") String email,
            @RequestParam("password") String password, HttpSession session,
            Model model) {
        LoginDto credentials = new LoginDto();
        credentials.setEmail(email);
        credentials.setPassword(password);
        ResponseEntity<ResponseModel<LoginResponseModel>> response = authenticationService.login(credentials);

        if (response.getStatusCode().is2xxSuccessful()) {
            String token = response.getBody().getData().getToken();
            session.setAttribute("auth_token", token);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("errorMessage", response.getBody().getMessage());
            return "login";
        }
    }

    @GetMapping("/addBook")
    public String addBookPage(Model model) {
        getGenres(model);
        return "addBook";
    }

    @GetMapping("/getBooks")
    public String getBooksPage(Model model) {
        getBooks(model);
        return "getBooks";
    }

    @GetMapping("/addGenre")
    public String addGenrePage() {
        return "addGenre";
    }

    private void getGenres(Model model) {
        ResponseEntity<ResponseModel<List<Genre>>> response = genreService.getAllGenres();
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Genre> genres = response.getBody().getData();
            model.addAttribute("genres", genres);
        } else {
            model.addAttribute("genres", new ArrayList<>());
        }
    }

    @PostMapping("/post-book")
    public ResponseEntity<ResponseModel<Map<String, Object>>> addBook(
            @Valid @ModelAttribute BookDto bookDto,
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("genres") String genres, Model model) {

        System.out.println("Posting");
        String errorMessage = validateImageFile(imageFile, model);
        if (errorMessage != null) {
            System.out.println(errorMessage);
            return ResponseUtil.badRequestResponse(errorMessage);
        }
        List<Long> genreIds = Arrays.stream(genres.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        var response = bookService.postBook(bookDto, imageFile, genreIds);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseUtil.createdResponse(null, response.getBody().getMessage());

        }
        return ResponseUtil.badRequestResponse(response.getBody().getMessage());
    }

    private String validateImageFile(MultipartFile imageFile, Model model) {
        if (imageFile.isEmpty()) {
            return "Please upload an image.";
        }
        String fileType = imageFile.getContentType();
        if (fileType == null || (!fileType.equals("image/jpeg") && !fileType.equals("image/jpg"))) {
            return "Only .jpg and .jpeg image files are allowed.";
        }
        long maxSize = 50 * 1024 * 1024; // 5 MB
        if (imageFile.getSize() > maxSize) {
            return "The file size exceeds the 50MB limit.";
        }
        return null;
    }

    @PostMapping("/post-genre")
    public ResponseEntity<ResponseModel<Map<String, Object>>> postGenre(
            @Valid @RequestBody GenreDto genre, Model model) {
        System.out.println("Posting Genre");
        System.out.println(genre.getName());
        var response = genreService.createGenre(genre);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseUtil.createdResponse(null, response.getBody().getMessage());

        }
        return ResponseUtil.badRequestResponse(response.getBody().getMessage());
    }

    @DeleteMapping("/delete-book/{bookId}")
    public ResponseEntity<ResponseModel<Book>> deleteBook(
            @PathVariable("bookId") long bookId) { // Bind bookId from the URL path
        if (bookId <= 0) {
            return ResponseUtil.badRequestResponse("Invalid book ID provided.");
        }

        ResponseEntity<ResponseModel<Book>> response = bookService.deleteBook(bookId);

        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseUtil.createdResponse(response.getBody().getData(), "Successfully deleted the book.");
        } else {
            return ResponseUtil.badRequestResponse(response.getBody().getMessage());

        }
    }

    private void getBooks(Model model) {
        ResponseEntity<ResponseModel<List<Book>>> response = bookService.getBook();
        if (response.getStatusCode().is2xxSuccessful()) {
            List<Book> books = response.getBody().getData();
            System.out.println("BOOKS : " + books);
            model.addAttribute("books", books);
        } else {
            model.addAttribute("books", new ArrayList<>());
        }
    }

}
