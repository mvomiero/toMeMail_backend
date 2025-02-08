package com.example.toMeMail.dto;

import com.example.toMeMail.entity.Message;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@Setter
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime dueDate;
    private LocalDateTime creationDate;
    private int senderAge;
    private int recipientAge;

    public MessageResponseDto(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.dueDate = message.getDueDate();
        this.creationDate = message.getCreationDate();

        // Calculate ages
        LocalDate birthDate = message.getUser().getDateOfBirth();
        this.senderAge = calculateAge(birthDate, creationDate.toLocalDate());
        this.recipientAge = calculateAge(birthDate, dueDate.toLocalDate());
    }

    private int calculateAge(LocalDate birthDate, LocalDate referenceDate) {
        return Period.between(birthDate, referenceDate).getYears();
    }
}
