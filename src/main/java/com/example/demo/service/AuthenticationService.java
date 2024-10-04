package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.model.LoginResponseModel;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ResponseUtil;

import dtos.LoginDto;
import dtos.RegisterDto;
import jakarta.validation.Valid;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<ResponseModel<UserModel>> signUp(@Valid @RequestBody RegisterDto newUser) {
        try {
            String email = newUser.getEmail();
            UserModel existingUser = userRepository.findByEmail(email)
                    .orElse(null);

            if (existingUser != null) {
                return ResponseUtil
                        .conflictResponse(String.format("User with the email '%s' already exists.", email));
            }
            System.out.println("Password Length: " + newUser.getPassword().length());
            UserModel user = new UserModel()
                    .setFullName(newUser.getFullName())
                    .setEmail(newUser.getEmail())
                    .setPassword(passwordEncoder.encode(newUser.getPassword()));

            UserModel registerUser = userRepository.save(user);
            return ResponseUtil.createdResponse(registerUser, "Registeration succesful!");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while registering user: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<LoginResponseModel>> login(@RequestBody LoginDto input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()));
            UserModel user = userRepository.findByEmail(input.getEmail())
                    .orElseThrow();

            if (user != null && passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                String jwtToken = jwtService.generateToken(user);
                LoginResponseModel loginResponseModel = new LoginResponseModel().setToken(jwtToken)
                        .setExpiresIn(jwtService.getExpirationTime());

                return ResponseUtil.successResponse(loginResponseModel, "Login succesful!");
            } else if (user != null && !passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                return ResponseUtil.unauthorizedResponse("Invalid Credentials!");

            }
            return ResponseUtil
                    .notFoundResponse(String.format("User with the email '%s' doesn/'t' exists.", input.getEmail()));

        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while registering user: " + e.getMessage());
        }
    }

}
