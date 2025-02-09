package com.example.toMeMail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MessageDto {

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Due date must be provided")
    private LocalDate dueDate;

}
