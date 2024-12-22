package com.example.toMeMail.controller;

import com.example.toMeMail.dto.LoginRequest;
import com.example.toMeMail.dto.RegisterRequest;
import com.example.toMeMail.security.util.JwtTokenUtil;
import com.example.toMeMail.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager; // Bean in SecurityConfig
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Extract the username from the authenticated token
        String username = authentication.getName();

        // Generate and return the JWT token
        return jwtTokenUtil.generateToken(username);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getRole()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> grantAccess() {
        return ResponseEntity.status(HttpStatus.OK).body("Access granted");
    }
}
