package com.example.toMeMail.controller;


import com.example.toMeMail.dto.MessageDto;
import com.example.toMeMail.dto.MessageResponseDto;
import com.example.toMeMail.entity.Message;
import com.example.toMeMail.security.CustomUserDetails;
import com.example.toMeMail.service.MessageService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Message> createMessage(@Valid @RequestBody MessageDto message) {

        Message createdMessage = messageService.createMessage(message);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponseDto> getMessageById(@PathVariable Long id) {

        Message message = messageService.getMessageById(id);
        return new ResponseEntity<>(new MessageResponseDto(message), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> getMessages() {

        List<Message> messages = messageService.getMessagesByUsername();
        return new ResponseEntity<>(messages.stream().map(MessageResponseDto::new).toList(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {

        messageService.deleteMessage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

