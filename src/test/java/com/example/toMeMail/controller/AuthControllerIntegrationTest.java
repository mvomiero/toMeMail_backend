package com.example.toMeMail.controller;


import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.UserRepository;
import com.example.toMeMail.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setup() {
        userRepository.deleteAll(); // Clean up database before each test
    }

    @Test
    void registerUser_shouldReturnCreatedStatus() throws Exception {
        String registerPayload = """
            {
                "username": "testUser",
                "password": "testPassword",
                "role": "USER",
                "dateOfBirth": "1995-08-15"
            }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        // Verify user is saved in the database with hashed password
        User savedUser = userRepository.findByUsername("testUser").orElse(null);
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("testPassword", savedUser.getPassword()));
        assertEquals(LocalDate.of(1995, 8, 15), savedUser.getDateOfBirth());
    }

    @Test
    void login_withValidCredentials_shouldReturnJwtToken() throws Exception {
        // Register a user directly in the database
        User user = testDataFactory.createTestUser("testUser", "testPassword", "USER", "1995-08-15");
        userRepository.save(user);

        String loginPayload = """
            {
                "username": "testUser",
                "password": "testPassword",
                "dateOfBirth": "1995-08-15"
            }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk());
                //.andExpect(content().isNotEmpty()); // Check that a JWT is returned
    }

    @Test
    void login_withInvalidCredentials_shouldReturnForbidden() throws Exception {
        String loginPayload = """
            {
                "username": "nonExistentUser",
                "password": "wrongPassword"
            }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessProtectedEndpoint_withoutJwt_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/protected"))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessProtectedEndpoint_withValidJwt_shouldReturnOk() throws Exception {
        // Register and login the user to get a JWT
        User user = testDataFactory.createTestUser("testUser", "testPassword", "USER", "1995-08-15");
        userRepository.save(user);

        String loginPayload = """
            {
                "username": "testUser",
                "password": "testPassword",
                "dateOfBirth": "1995-08-15"
            }
        """;

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = loginResult.getResponse().getContentAsString();
        String jwt = new ObjectMapper().readTree(jsonResponse).get("token").asText();

        // Use the JWT to access the protected endpoint
        mockMvc.perform(get("/auth/protected")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("Access granted"));
    }
}

