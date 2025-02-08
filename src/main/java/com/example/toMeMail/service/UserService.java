package com.example.toMeMail.service;

import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String username, String rawPassword, String role, LocalDate dateOfBirth) {
        // Check if the user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create a new user and save it to the database
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setDateOfBirth(dateOfBirth);
        return userRepository.save(user);
    }
}

