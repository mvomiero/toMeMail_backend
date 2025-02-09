-- Insert Users with pre-hashed passwords
INSERT INTO users (user_name, password, role, date_of_birth) VALUES
('alice', '$2a$10$P6BqKbu29rxQkzB2cPX.z.oANqBZ/TpQxA63HKUeYqGuxKKzBHiXC', 'USER', '1995-08-15');
-- password = 'testPassword'

-- Insert Messages linked to users
INSERT INTO messages (content, due_date, creation_date, user_id) VALUES
('Hello Future Alice!', '2025-01-01 10:00:00', '2015-01-01 10:00:00', 1),
('Remember to invest!', '2030-06-15 12:00:00', '2015-01-01 10:00:00', 1);

