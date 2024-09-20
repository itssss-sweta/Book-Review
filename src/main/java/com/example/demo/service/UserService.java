package com.example.demo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.ResponseModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.ResponseUtil;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<ResponseModel<UserModel>> signUp(UserModel newUser) {
        try {
            String email = newUser.getEmail();
            UserModel existingUser = userRepository.findByEmail(email);

            if (existingUser == null) {
                return ResponseUtil
                        .conflictResponse(String.format("User with the email '%s' already exists.", email));
            }
            String hashedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(hashedPassword);
            UserModel registerUser = userRepository.save(newUser);
            return ResponseUtil.createdResponse(registerUser, "Registeration succesful!");
        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while registering user: " + e.getMessage());
        }
    }

    public ResponseEntity<ResponseModel<UserModel>> login(String email, String password) {
        try {
            UserModel user = userRepository.findByEmail(email);

            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return ResponseUtil.successResponse(user, "Login succesful!");
            } else if (user != null && !passwordEncoder.matches(password, user.getPassword())) {
                return ResponseUtil.unauthorizedResponse("Invalid Credentials!");

            }
            return ResponseUtil
                    .notFoundResponse(String.format("User with the email '%s' doesn/'t' exists.", email));

        } catch (Exception e) {
            return ResponseUtil.serverErrorResponse("An error occurred while registering user: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Return a UserDetails object (could be your custom User object or a Spring
        // Security object)
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole())));
    }
}
