-- Insert users
INSERT INTO USER (username, email, password, role) VALUES ('john_doe', 'john@example.com', 'password123', 'USER');
INSERT INTO USER (username, email, password, role) VALUES ('jane_doe', 'jane@example.com', 'password123', 'USER');

-- Insert messages
INSERT INTO MESSAGE (content, due_date, user_id) VALUES ('Hello John in the future!', '2030-01-01 10:00:00', 1);
INSERT INTO MESSAGE (content, due_date, user_id) VALUES ('Hello Jane in the future!', '2035-01-01 10:00:00', 2);
