package com.example.toMeMail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MessageAccessBeforeDueDateException extends RuntimeException {

    public static final String MESSAGE = "The due date for this message has not passed yet";

    public MessageAccessBeforeDueDateException(String message) {
        super(message);
    }
}
