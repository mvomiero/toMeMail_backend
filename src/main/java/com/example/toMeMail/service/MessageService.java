package com.example.toMeMail.service;

import com.example.toMeMail.dto.MessageRequestDto;
import com.example.toMeMail.entity.Message;

import java.util.List;

public interface MessageService {

    Message createMessage(MessageRequestDto message);
    List<Message> getMessagesByUsername();
    Message getMessageById(Long messageId);
    void deleteMessage(Long messageId);
}
