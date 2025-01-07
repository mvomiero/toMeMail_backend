# JWT Authentication in toMeMail

## Introduction

JSON Web Tokens (JWT) are a compact and secure way to represent claims between two parties. They are commonly used for authentication and session management in modern web applications. JWTs consist of three parts:

1. **Header**: Specifies the algorithm used for signing the token.
2. **Payload**: Contains the claims, such as user information and token expiration time.
3. **Signature**: Ensures the token's integrity by signing the header and payload using a secret key.

In the toMeMail application, JWT is used to authenticate users and secure API endpoints.

---

## Authentication Flow

The authentication process in toMeMail involves several steps, from user login to token validation for subsequent requests. Here's a breakdown of the flow:

### 1. User Login

When a user logs in via the `/auth/login` endpoint:
- The **AuthController** authenticates the user credentials using Spring's `AuthenticationManager`.
- Upon successful authentication, a JWT is generated using the `JwtTokenUtil` class. This token contains the username and expiration information.
- The token is returned to the user and should be included in subsequent requests as a Bearer token in the `Authorization` header.

### 2. Securing API Endpoints

The `SecurityConfig` class configures Spring Security to:
- Allow unauthenticated access to specific endpoints, such as `/auth/**` and OpenAPI documentation.
- Require authentication for all other endpoints.
- Disable CSRF (since JWT provides its own security mechanisms).
- Set session management to stateless, as JWT eliminates the need for server-side sessions.

### 3. Request Filtering

Every incoming request passes through the `JwtFilter`, which:
- Extracts the JWT from the `Authorization` header.
- Validates the token using `JwtTokenUtil` (e.g., checking expiration and signature).
- Retrieves user details from the database via `CustomUserDetailsService`.
- Sets the `SecurityContext` with the authenticated user's information if the token is valid.

### 4. Token Validation

The `JwtTokenUtil` class handles the validation and management of tokens. Key methods include:
- **generateToken**: Creates a JWT with a subject (username) and expiration.
- **validateToken**: Ensures the token's signature is valid and checks if it has expired.
- **extractUsername**: Retrieves the username encoded in the token.

### 5. Custom User Management

User details are managed using:
- `CustomUserDetails`: Implements Spring Security's `UserDetails` interface.
- `UserService`: Handles user registration with encrypted passwords using `BCryptPasswordEncoder`.
- `UserRepository`: Interacts with the database to store and retrieve user information.

---

## Key Components

### Security Configuration
The `SecurityConfig` class ensures all endpoints are protected and integrates the `JwtFilter`.
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

### JWT Filter
The `JwtFilter` ensures that requests are authenticated before reaching the controllers.
```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String token = authorizationHeader.substring(7);
        String username = jwtTokenUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }

    chain.doFilter(request, response);
}
```

---

## Summary

JWT in toMeMail streamlines authentication and secures API endpoints by:
1. Generating tokens during login.
2. Validating tokens for every request using a custom filter.
3. Eliminating the need for server-side sessions, making the application more scalable.

This setup leverages Spring Security's robust capabilities while providing a foundation for future enhancements, such as token refresh mechanisms or role-based access control.

