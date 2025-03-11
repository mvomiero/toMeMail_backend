package com.example.toMeMail.service;

import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String rawPassword, String role, LocalDate dateOfBirth) {
        // Check if the user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create a new user and save it to the database
        User user = new User(username, encodedPassword, role, dateOfBirth);
        return userRepository.save(user);
    }
}

