package com.example.toMeMail.controller;


import com.example.toMeMail.dto.MessageRequestDto;
import com.example.toMeMail.dto.MessageResponseDto;
import com.example.toMeMail.entity.Message;
import com.example.toMeMail.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> createMessage(@Valid @RequestBody MessageRequestDto message) {

        Message createdMessage = messageService.createMessage(message);
        return new ResponseEntity<>(new MessageResponseDto(createdMessage), HttpStatus.CREATED);
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

