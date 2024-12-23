package com.example.toMeMail.service;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.exception.UserNotFoundException;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @Transactional
    public Message createMessage(Message message, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User " + username + " not found"));
        message.setUser(user);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByUsername(String username) {
        return messageRepository.findByUserUsername(username);
    }

    @Override
    public Message getMessageById(Long messageId, String username) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new UserNotFoundException("User with ID " + username + " not found"));
        /*if (message.getUser().getId() != userId) {
            throw new RuntimeException("No authorized to access this message!");
        }*/
        return message;
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId, String username) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));

        messageRepository.delete(message);
    }
}
