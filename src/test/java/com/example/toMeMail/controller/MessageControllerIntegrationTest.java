package com.example.toMeMail.controller;

import com.example.toMeMail.entity.Message;
import com.example.toMeMail.entity.User;
import com.example.toMeMail.exception.MessageAccessBeforeDueDateException;
import com.example.toMeMail.repository.MessageRepository;
import com.example.toMeMail.repository.UserRepository;
import com.example.toMeMail.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private TestDataFactory testDataFactory;

    private String jwtToken;

    private User testUser;

    @BeforeEach
    void setup() throws Exception {
        messageRepository.deleteAll();
        userRepository.deleteAll();

        testUser = testDataFactory.createTestUser("TestUser", "password123", "USER", "1995-08-15");

        // Generate JWT
        jwtToken = generateJwtToken(testUser.getUsername());
    }

    private String generateJwtToken(String username) throws Exception {

        String jsonResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "%s",
                          "password": "password123",
                          "dateOfBirth": "1995-08-15"
                        }
                        """.formatted(username)))
                .andReturn()
                .getResponse()
                .getContentAsString(); // Adjust based on your JWT response format

        return new ObjectMapper().readTree(jsonResponse).get("token").asText();
    }

    @Test
    void createMessage_shouldReturnCreated() throws Exception {
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
    void createMessage_withNoContent_shouldReturnBadRequest() throws Exception {
        String newMessage = """
                {
                    "content": "",
                    "dueDate": "2024-12-31T23:59:59"
                }
                """;

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(newMessage))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.content").value("Content cannot be empty"));
    }

    @Test
    void getMessageById_shouldReturnOk() throws Exception {

        Message message = testDataFactory.createTestMessage("Test Message Content", LocalDate.now(), testUser);

        mockMvc.perform(get("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test Message Content"));
    }

    @Test
    void getMessageById_withLaterDate_shouldReturnForbidden() throws Exception {

        Message message = testDataFactory.createTestMessage("Test Message Content", LocalDate.now().plusYears(100), testUser);

        mockMvc.perform(get("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(MessageAccessBeforeDueDateException.MESSAGE));

    }

    @Test
    void getMessageById_withWrongUser_shouldReturnForbidden() throws Exception {

        User user2 = testDataFactory.createTestUser("TestUser2", "password123", "USER", "1995-08-15");

        Message message = testDataFactory.createTestMessage("Message 1", LocalDate.now(), user2);

        mockMvc.perform(get("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void getMessages_shouldReturnOk() throws Exception {

        testDataFactory.createTestMessage("Message 1", LocalDate.now(), testUser);
        testDataFactory.createTestMessage("Message 2", LocalDate.now(), testUser);

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
    void deleteMessage_shouldReturnOkNoContent() throws Exception {

        Message message = testDataFactory.createTestMessage("Message 1", LocalDate.now(), testUser);

        mockMvc.perform(delete("/messages/" + message.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

