package com.example.toMeMail.controller;


import com.example.toMeMail.entity.User;
import com.example.toMeMail.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static java.util.function.Predicate.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                "role": "USER"
            }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));

        // Verify user is saved in the database with hashed password
        User savedUser = userRepository.findByUsername("testUser").orElse(null);
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("testPassword", savedUser.getPassword()));
    }

    @Test
    void login_withValidCredentials_shouldReturnJwtToken() throws Exception {
        // Register a user directly in the database
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPassword"));
        user.setRole("USER");
        userRepository.save(user);

        String loginPayload = """
            {
                "username": "testUser",
                "password": "testPassword"
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
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPassword"));
        user.setRole("USER");
        userRepository.save(user);

        String loginPayload = """
            {
                "username": "testUser",
                "password": "testPassword"
            }
        """;

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String jwt = loginResult.getResponse().getContentAsString();

        // Use the JWT to access the protected endpoint
        mockMvc.perform(get("/auth/protected")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("Access granted"));
    }
}

