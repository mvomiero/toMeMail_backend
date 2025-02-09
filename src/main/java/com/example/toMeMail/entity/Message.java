package com.example.toMeMail.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    // maps many messages to one user
    // Hibernate sees that a Message has a single User (user field),
    // and this is stored in the user_id foreign key column.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now(); // Automatically set creation date
    }

}
