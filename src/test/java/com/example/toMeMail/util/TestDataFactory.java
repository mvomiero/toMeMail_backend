package com.example.toMeMail.util;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TestDataFactory {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;

    public User createTestUser(String username, String password, String role, String birthDate) {
        User user = new User(username, passwordEncoder.encode(password), role, LocalDate.parse(birthDate));
        return userRepository.save(user);
    }

    public Message createTestMessage(String content, LocalDate dueDate, User user) {
        Message message = new Message(content, dueDate, user);
        return messageRepository.save(message);
    }
}
