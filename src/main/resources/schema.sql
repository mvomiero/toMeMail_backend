DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,  -- Works in both H2 (MSSQL mode) & Azure SQL
    user_name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL
);

CREATE TABLE messages (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    content TEXT NOT NULL,
    due_date DATETIME NOT NULL,
    creation_date DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
