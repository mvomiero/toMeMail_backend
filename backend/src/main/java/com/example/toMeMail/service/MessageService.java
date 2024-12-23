package com.example.toMeMail.service;

import com.example.toMeMail.entity.Message;

import java.util.List;

public interface MessageService {

    Message createMessage(Message message, String username);
    List<Message> getMessagesByUsername(String username);
    Message getMessageById(Long messageId, String username);
    void deleteMessage(Long messageId, String username);
}
