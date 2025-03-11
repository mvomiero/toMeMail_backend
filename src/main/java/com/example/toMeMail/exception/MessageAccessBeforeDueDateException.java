package com.example.toMeMail.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MessageAccessBeforeDueDateException extends RuntimeException {
    public static final String MESSAGE = "The due date for this message has not passed yet";
    private final LocalDate dueDate;

    public MessageAccessBeforeDueDateException(LocalDate dueDate) {
        super(MESSAGE);
        this.dueDate = dueDate;
    }
}
