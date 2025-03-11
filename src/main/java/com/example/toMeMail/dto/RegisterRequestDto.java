package com.example.toMeMail.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    //@NotBlank(message = "Role cannot be empty")
    @Nullable
    private String role;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}

