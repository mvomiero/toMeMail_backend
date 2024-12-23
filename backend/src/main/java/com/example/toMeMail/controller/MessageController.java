package com.example.toMeMail.controller;


import com.example.toMeMail.entity.Message;
import com.example.toMeMail.security.CustomUserDetails;
import com.example.toMeMail.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        String username = getAuthenticatedUsername();
        Message createdMessage = messageService.createMessage(message, username);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        String username = getAuthenticatedUsername();
        Message message = messageService.getMessageById(id, username);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages() {
        String username = getAuthenticatedUsername();
        List<Message> messages = messageService.getMessagesByUsername(username);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        String username = getAuthenticatedUsername();
        messageService.deleteMessage(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}

