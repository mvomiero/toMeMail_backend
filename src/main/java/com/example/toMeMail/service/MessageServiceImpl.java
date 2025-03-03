package com.example.toMeMail.service;

import com.example.toMeMail.dto.MessageDto;
import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.exception.MessageAccessBeforeDueDateException;
import com.example.toMeMail.exception.UserNotFoundException;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import com.example.toMeMail.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @Transactional
    public Message createMessage(MessageDto messageDto) {

        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setDueDate(messageDto.getDueDate());

        User user = userRepository.findByUsername(getAuthenticatedUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        message.setUser(user);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByUsername() {
        return messageRepository.findByUserUsername(getAuthenticatedUsername());
    }

    @Override
    public Message getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!message.getUser().getUsername().equals(getAuthenticatedUsername())) {
            throw new AccessDeniedException("You are not authorized to access this message");
        }
        if (message.getDueDate().isAfter(LocalDate.now())) {
            throw new MessageAccessBeforeDueDateException(MessageAccessBeforeDueDateException.MESSAGE);
        }
        return message;
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getUser().getUsername().equals(getAuthenticatedUsername())) {
            throw new AccessDeniedException("You are not authorized to access this message");
        }

        messageRepository.delete(message);
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
