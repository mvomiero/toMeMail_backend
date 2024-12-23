package com.example.toMeMail.controller;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import com.example.toMeMail.service.CustomUserDetailsService;
import com.example.toMeMail.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;

    @BeforeEach
    void setup() throws Exception {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user and encode password
        User testUser = new User();
        testUser.setUsername("TestUser");
        testUser.setPassword(passwordEncoder.encode("password123")); // Encode the password
        testUser.setRole("USER");
        userRepository.save(testUser);

        // Generate JWT
        jwtToken = generateJwtToken(testUser.getUsername());
        System.out.println("Generated JWT Token: " + jwtToken);


    }

    private String generateJwtToken(String username) throws Exception {
        // Mock JWT generation logic or use a real JWT creation utility if available
        return mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "%s",
                          "password": "password123"
                        }
                        """.formatted(username)))
                .andReturn()
                .getResponse()
                .getContentAsString(); // Adjust based on your JWT response format
    }

    @Test
    void createMessage() throws Exception {
        String newMessage = """
                {
                    "content": "Integration test message",
                    "dueDate": "2024-12-31T23:59:59"
                }
                """;

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(newMessage))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Integration test message"));
    }

    @Test
    void getMessageById() throws Exception {
        Message message = new Message();
        message.setContent("Test Message Content");
        message.setDueDate(LocalDateTime.of(2024, 12, 31, 23, 59, 59));
        message.setUser(userRepository.findByUsername("TestUser").orElseThrow());
        messageRepository.save(message);

        mockMvc.perform(get("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test Message Content"));
    }

    @Test
    void getMessages() throws Exception {
        User user = userRepository.findByUsername("TestUser").orElseThrow();
        messageRepository.save(new Message("Message 1", LocalDateTime.now(), user));
        messageRepository.save(new Message("Message 2", LocalDateTime.now(), user));

        mockMvc.perform(get("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].content").value("Message 1"))
                .andExpect(jsonPath("$[1].content").value("Message 2"));
    }

    @Test
    void deleteMessage() throws Exception {
        User user = userRepository.findByUsername("TestUser").orElseThrow();
        Message message = messageRepository.save(new Message("Message 1", LocalDateTime.now(), user));

        mockMvc.perform(delete("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

