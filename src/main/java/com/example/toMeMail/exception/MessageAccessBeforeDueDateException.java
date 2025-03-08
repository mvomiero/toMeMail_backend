package com.example.toMeMail.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@Getter
public class MessageAccessBeforeDueDateException extends RuntimeException {
    public static final String MESSAGE = "The due date for this message has not passed yet";
    private final LocalDate dueDate;

    public MessageAccessBeforeDueDateException(String message, LocalDate dueDate) {
        super(message);
        this.dueDate = dueDate;
    }
}
