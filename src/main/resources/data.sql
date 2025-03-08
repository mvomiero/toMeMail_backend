-- Insert Test User (password = 'TestPassword' hashed with BCrypt)
INSERT INTO users (user_name, password, role, date_of_birth) VALUES
('testUser', '$2a$10$P6BqKbu29rxQkzB2cPX.z.oANqBZ/TpQxA63HKUeYqGuxKKzBHiXC', 'USER', '1983-05-31');

-- Insert Example Messages for testUser
-- Messages from the past (already accessible)
INSERT INTO messages (content, due_date, creation_date, user_id) VALUES
('Hey future me! You just turned 30. I hope you’re chasing your dreams and not settling for less. Do you still love hiking as much as I do now? Keep exploring, keep learning.',
 '2013-05-31', '2003-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('Hello me at 40! I hope you’ve found balance between work and passion. Did you finally write that book? If not, start today. Never let routine steal your creativity.',
 '2023-05-31', '2003-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('Sixty already? Wow. I wonder if you’ve become the wise person younger me hoped for. Have you forgiven more than you held grudges? If not, it’s never too late.',
 '2043-05-31', '2003-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('So, 35… I hope you’ve figured out some things by now! Please tell me you’re not afraid to make mistakes. You always wanted adventure—have you taken risks? Stay bold.',
 '2018-05-31', '2009-05-31', (SELECT id FROM users WHERE user_name = 'testUser'));

-- Messages for the future (locked)
INSERT INTO messages (content, due_date, creation_date, user_id) VALUES
('Dear 70-year-old me, I hope you’ve built a life that makes you smile. I hope you still dance, even if your knees don’t approve. Have you stayed in touch with old friends?',
 '2053-05-31', '2009-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('Hey 50-year-old me! You once feared getting older—silly, right? I hope you’re living with fewer regrets and embracing your age. Have you learned to love silence as much as laughter?',
 '2033-05-31', '2013-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('Eighty. I can’t imagine that. Are you happy? Have you told people you love them enough? If not, do it now. Hug someone today.',
 '2063-05-31', '2013-05-31', (SELECT id FROM users WHERE user_name = 'testUser')),

('To me at 35: I hope you remember that life is not a checklist. It’s okay if things don’t go as planned. Just don’t stop dreaming.',
 '2018-05-31', '2013-05-31', (SELECT id FROM users WHERE user_name = 'testUser'));
