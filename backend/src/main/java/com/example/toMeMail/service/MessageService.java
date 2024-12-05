package com.example.toMeMail.service;

import com.example.toMeMail.entity.Message;

import java.util.List;

public interface MessageService {

    Message createMessage(Message message, Long userId);
    List<Message> getMessagesByUser(Long userId);
    Message getMessageById(Long messageId, Long userId);
    void deleteMessage(Long messageId, Long userId);
}
