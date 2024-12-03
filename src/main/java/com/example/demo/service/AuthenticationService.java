package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.dtos.LoginDto;
import com.example.demo.dtos.RegisterDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.model.LoginResponseModel;
import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.AuthenticationRepository;
import com.example.demo.utils.ResponseUtil;

import jakarta.validation.Valid;

@Service
public class AuthenticationService {

    private final AuthenticationRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<ResponseModel<UserDto>> signUp(@Valid @RequestBody RegisterDto newUser) {
        try {
            String email = newUser.getEmail();
            UserModel existingUser = userRepository.findByEmail(email)
                    .orElse(null);

            if (existingUser != null) {
                return ResponseUtil
                        .conflictResponse(String.format("User with the email '%s' already exists.", email));
            }
            UserModel user = new UserModel()
                    .setFullName(newUser.getFullName())
                    .setEmail(newUser.getEmail())
                    .setPassword(passwordEncoder.encode(newUser.getPassword()));

            UserModel registerUser = userRepository.save(user);
            UserDto userDto = new UserDto();
            userDto.setUid(registerUser.getUid());
            userDto.setFullName(registerUser.getFullName());
            userDto.setEmail(registerUser.getEmail());
            userDto.setCreatedAt(registerUser.getCreatedAt());
            userDto.setUpdatedAt(registerUser.getUpdatedAt());
            return ResponseUtil.createdResponse(userDto, "Registeration successful.");
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
                    .orElse(null);

            if (user != null && passwordEncoder.matches(input.getPassword(), user.getPassword())) {
                String jwtToken = jwtService.generateToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);

                UserDto userDto = new UserDto();
                userDto.setUid(user.getUid());
                userDto.setFullName(user.getFullName());
                userDto.setEmail(user.getEmail());
                userDto.setCreatedAt(user.getCreatedAt());
                userDto.setUpdatedAt(user.getUpdatedAt());

                LoginResponseModel loginResponseModel = new LoginResponseModel()
                        .setToken(jwtToken)
                        .setExpiresIn(jwtService.getExpirationTime())
                        .setUserModel(userDto).setRefreshToken(refreshToken);
                return ResponseUtil.successResponse(loginResponseModel, "Login successful!");
            }

            return ResponseUtil
                    .notFoundResponse(String.format("User with the email '%s' doesn/'t' exists.", input.getEmail()));

        } catch (BadCredentialsException e) {
            return ResponseUtil.unauthorizedResponse("Invalid Credentials!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.serverErrorResponse("An error occurred while logging in: " + e.getMessage());
        }
    }

}
