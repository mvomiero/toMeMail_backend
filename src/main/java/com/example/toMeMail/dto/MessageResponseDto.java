package com.example.toMeMail.dto;

import com.example.toMeMail.entity.Message;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDate dueDate;
    private LocalDate creationDate;
    private int senderAge;
    private int recipientAge;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.dueDate = message.getDueDate();
        this.creationDate = message.getCreationDate();

        // Calculate ages
        LocalDate birthDate = message.getUser().getDateOfBirth();
        this.senderAge = calculateAge(birthDate, creationDate);
        this.recipientAge = calculateAge(birthDate, dueDate);
    }

    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return Period.between(birthDate, referenceDate).getYears();
    }
}
