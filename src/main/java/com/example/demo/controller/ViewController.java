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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BookDto;
import com.example.demo.dtos.LoginDto;
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
    public String getBooksPage() {
        return "getBooks";
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
            model.addAttribute("alert", errorMessage);
            return ResponseUtil.badRequestResponse(errorMessage);
        }
        List<Long> genreIds = Arrays.stream(genres.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        var response = bookService.postBook(bookDto, imageFile, genreIds);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseUtil.createdResponse(null, response.getBody().getMessage());

        }
        model.addAttribute("alertMessage", response.getBody().getMessage());
        model.addAttribute("alertTitle", "Book Upload!");
        return ResponseUtil.badRequestResponse(response.getBody().getMessage());
    }

    private String validateImageFile(MultipartFile imageFile, Model model) {
        model.addAttribute("alertTitle", "Image Upload!");

        if (imageFile.isEmpty()) {
            model.addAttribute("alertMessage", "Please upload an image.");
            return "Please upload an image.";
        }
        String fileType = imageFile.getContentType();
        if (fileType == null || (!fileType.equals("image/jpeg") && !fileType.equals("image/jpg"))) {
            model.addAttribute("alertMessage", "Only .jpg and .jpeg image files are allowed.");
            return "Only .jpg and .jpeg image files are allowed.";
        }
        long maxSize = 50 * 1024 * 1024; // 5 MB
        if (imageFile.getSize() > maxSize) {
            model.addAttribute("alertMessage", "The file size exceeds the 50MB limit.");
            return "The file size exceeds the 50MB limit.";
        }
        return null;
    }

}
