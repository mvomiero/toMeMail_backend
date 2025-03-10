# toMeMail

**toMeMail** is a simple web application designed to send messages to yourself or your loved ones in the future. These messages are locked until their due date, fostering introspection and long-term reflection.

_While time travel between different versions of yourself isn’t possible, toMeMail offers the next best thing: sending a message into the future and waiting for time to pass. For example, your 30-year-old self can write a message to your 60-year-old self, creating a bridge for introspection and self-discovery._

[![Coverage Status](https://coveralls.io/repos/github/mvomiero/toMeMail_backend/badge.svg?branch=main)](https://coveralls.io/github/mvomiero/toMeMail_backend?branch=main)
[![Build & Test](https://github.com/mvomiero/toMeMail_backend/actions/workflows/build_test.yml/badge.svg)](https://github.com/mvomiero/toMeMail_backend/actions/workflows/build_test.yml)
[![Deploy Backend](https://github.com/mvomiero/toMeMail_backend/actions/workflows/deploy-backend.yml/badge.svg)](https://github.com/mvomiero/toMeMail_backend/actions/workflows/deploy-backend.yml)

### 🌟 Features

- **Message Delivery to the Future**: Send messages to your future self with a due date. These messages remain locked until the specified date arrives.
- **Introspection and Growth**: Encourage personal growth by reflecting on messages written years ago.

### 🚀 Current Backend Features

The backend is developed using **Java Spring Boot** and provides the following functionality:

1. **CRUD Operations for Messages**:
   - Basic API to create, read, update, and delete messages.
   - Includes corresponding controller, service, and entity layers.

2. **Authentication and User Management**:
   - User login and registration.
   - Security implemented with **JWT (JSON Web Tokens)**:
     - Custom JWT filter.
     - `UserDetails` implementation.
     - Security filter chain and password encoder.
   - Spring Security integration.

3. **Database Management**:
   - Managed with **JPA Repository**.

4. **Testing**:
   - Comprehensive **integration tests** for backend functionality.

5. **Documentation**:
   - **OpenAPI** documentation for the API.

6. **Exception Handling**:
   - Custom exception handling for better error management.

7. **CI/CD**:
   - Automated builds and tests using **GitHub Actions**.
   - Secure secrets management integrated into workflows.

8. **Validation**:
   - Using springboot starter validation.

---

### 📂 Documentation

- **[JWT Security Implementation](docs/jwt-security.md)**

---

### 🛠️ Tech Stack

- **Backend**: Java Spring Boot
- **Security**: Spring Security, JWT
- **CI/CD**: GitHub Actions

---

### 📈 Future Plans

- Add the ability to send messages to loved ones.
- Frontend development for a user-friendly interface (Angular or React).
- Deploy the application to a cloud platform.

---

### 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request or open an Issue for suggestions.

---

### 📜 License

This project is licensed under the [MIT License](LICENSE).

---

### 🌍 About

Crafted with ❤️ to promote introspection and personal growth.
