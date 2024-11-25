package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dtos.LoginDto;
import com.example.demo.model.LoginResponseModel;
import com.example.demo.model.ResponseModel;
import com.example.demo.service.AuthenticationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {
    @Autowired
    private AuthenticationService authenticationService;

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
    public String addBookPage() {
        return "addBook";
    }

    @GetMapping("/getBooks")
    public String getBooksPage() {
        return "getBooks";
    }

}
