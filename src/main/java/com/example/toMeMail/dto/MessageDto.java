package com.example.toMeMail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Due date must be provided")
    private LocalDateTime dueDate;

}
