package com.example.toMeMail.controller;


import com.example.toMeMail.entity.Message;
import com.example.toMeMail.service.MessageService;
import com.example.toMeMail.service.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message, @RequestParam Long userId) {
        Message createdMessage = messageService.createMessage(message, userId);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id, @RequestParam Long userId) {
        Message message = messageService.getMessageById(id, userId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestParam Long userId) {
        List<Message> messages = messageService.getMessagesByUser(userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id, @RequestParam Long userId) {
        messageService.deleteMessage(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
