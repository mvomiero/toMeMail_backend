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
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setup() {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        testUser = TestDataFactory.createTestUser("Test User", "password123", userRepository);
    }

    @Test
    void createMessage() throws Exception {

        String newMessage = """
                {
                    "content": "Integration test message",
                    "dueDate": "2024-12-31T23:59:59"
                }
                """;

        mockMvc.perform(post("/messages?userId=" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newMessage))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Integration test message"))
                .andExpect(jsonPath("$.user.id").value(testUser.getId()));
    }

    @Test
    void getMessageNull() throws Exception {

        // No user in the database

        mockMvc.perform(get("/messages/" + 1L + "?userId=" + 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not Found"));
    }

    @Test
    void getMessageById() throws Exception {

        Message message = TestDataFactory.createTestMessage(
                "Test Message Content",
                LocalDateTime.of(2024, 12, 31, 23, 59, 59),
                testUser,
                messageRepository
        );

        mockMvc.perform(get("/messages/" + message.getId() + "?userId=" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test Message Content"))
                .andExpect(jsonPath("$.id").value(message.getId()))
                .andExpect(jsonPath("$.user.id").value(message.getUser().getId()));
    }

    @Test
    void getMessages() throws Exception {

        TestDataFactory.createTestMessage("Message 1", LocalDateTime.now(), testUser, messageRepository);
        TestDataFactory.createTestMessage("Message 2", LocalDateTime.now(), testUser, messageRepository);

        mockMvc.perform(get("/messages?userId=" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].content").value("Message 1"))
                .andExpect(jsonPath("$[1].content").value("Message 2"));
    }

    @Test
    public void contextLoads() {
        assertNotNull(customUserDetailsService, "CustomUserDetailsService bean should be loaded");
    }

    @Test
    void deleteMessage() throws Exception {

        Message message = TestDataFactory.createTestMessage(
                "Message 1",
                LocalDateTime.now(),
                testUser,
                messageRepository
        );

        mockMvc.perform(delete("/messages/" + message.getId() + "?userId=" + testUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


}
