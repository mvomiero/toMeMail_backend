-- Insert Test User (password = 'TestPassword' hashed with BCrypt)
INSERT INTO users (user_name, password, role, date_of_birth) VALUES
('testUser', '$2a$10$P6BqKbu29rxQkzB2cPX.z.oANqBZ/TpQxA63HKUeYqGuxKKzBHiXC', 'USER', '2000-01-01');

-- Insert Example Messages for testUser
INSERT INTO messages (content, due_date, creation_date, user_id) VALUES
('Welcome to the Demo!', '2025-12-31 23:59:59', '2023-12-31 23:59:59',
  (SELECT id FROM users WHERE user_name = 'testUser'));

