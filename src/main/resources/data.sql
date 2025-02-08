-- Insert Users with pre-hashed passwords
INSERT INTO users (id, user_name, password, role, date_of_birth) VALUES
(1, 'alice', '$2a$10$P6BqKbu29rxQkzB2cPX.z.oANqBZ/TpQxA63HKUeYqGuxKKzBHiXC', 'USER', '1995-08-15');
-- password = 'testPassword'

-- Insert Messages linked to users
INSERT INTO messages (id, content, due_date, creation_date, user_id) VALUES
(1, 'Hello Future Alice!', '2025-01-01 10:00:00', '2015-01-01 10:00:00', 1),
(2, 'Remember to invest!', '2030-06-15 12:00:00', '2015-01-01 10:00:00', 1);
