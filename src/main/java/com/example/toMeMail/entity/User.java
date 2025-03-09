package com.example.toMeMail.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // maps one user to many messages
    // The List<Message> in the User entity corresponds to a collection of rows in the messages table
    // all with the same user_id

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages;

    public User(String username, String password, String role, LocalDate dateOfBirth) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

}
