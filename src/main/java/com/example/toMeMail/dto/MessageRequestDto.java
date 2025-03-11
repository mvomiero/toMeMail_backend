package com.example.toMeMail.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MessageRequestDto {

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Due date must be provided")
    private LocalDate dueDate;

}
