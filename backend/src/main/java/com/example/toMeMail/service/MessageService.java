package com.example.toMeMail.service;

import com.example.toMeMail.entity.Message;

import java.util.List;

public interface MessageService {

    Message createMessage(Message message);
    List<Message> getMessagesByUsername();
    Message getMessageById(Long messageId);
    void deleteMessage(Long messageId);
}
