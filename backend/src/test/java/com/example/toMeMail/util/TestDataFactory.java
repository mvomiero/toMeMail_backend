package com.example.toMeMail.util;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TestDataFactory {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;

    public User createTestUser(String username, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        user.setRole(role); // Default role
        return userRepository.save(user);
    }

    public Message createTestMessage(String content, LocalDateTime dueDate, User user) {
        Message message = new Message();
        message.setContent(content);
        message.setDueDate(dueDate);
        message.setUser(user);
        return messageRepository.save(message);
    }
}
