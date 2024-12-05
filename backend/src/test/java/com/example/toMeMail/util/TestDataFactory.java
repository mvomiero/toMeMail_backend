package com.example.toMeMail.util;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;

import java.time.LocalDateTime;

public class TestDataFactory {

    public static User createTestUser(String username, String password, UserRepository repository) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return repository.save(user);
    }

    public static Message createTestMessage(String content, LocalDateTime dueDate, User user, MessageRepository repository) {
        Message message = new Message();
        message.setContent(content);
        message.setDueDate(dueDate);
        message.setUser(user);
        return repository.save(message);
    }
}
