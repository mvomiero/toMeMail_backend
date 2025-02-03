package com.example.toMeMail.controller;

import com.example.toMeMail.dto.LoginRequestDto;
import com.example.toMeMail.dto.LoginResponseDto;
import com.example.toMeMail.dto.RegisterRequestDto;
import com.example.toMeMail.dto.RegisterResponseDto;
import com.example.toMeMail.security.util.JwtTokenUtil;
import com.example.toMeMail.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager; // Bean in SecurityConfig
    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String username = authentication.getName();
        String token = jwtTokenUtil.generateToken(username);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody RegisterRequestDto registerRequest) {

        String role = (registerRequest.getRole() == null || registerRequest.getRole().isBlank()) ? "USER" : registerRequest.getRole();

        try {
            userService.registerUser(
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    role
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponseDto("User registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseDto(e.getMessage()));
        }
    }

    @GetMapping("/protected")
    public ResponseEntity<String> grantAccess() {
        return ResponseEntity.status(HttpStatus.OK).body("Access granted");
    }
}
