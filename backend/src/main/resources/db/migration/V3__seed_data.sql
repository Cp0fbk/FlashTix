-- FlashTix Seed Data Migration
-- =====================================================

-- 1. Initial Admin Account
-- Email: admin@flashtix.com
-- Password: Admin@123 (BCrypt encoded)
INSERT INTO users (email, password, role) 
VALUES ('admin@flashtix.com', '$2a$10$XFM871.K950fH.q6uHInveHBK8IIn.fD.eS6GlnT/Iu6vY26X19.G', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- 2. Initial Events
INSERT INTO events (title, location, start_time, end_time, banner_url) VALUES 
('Summer Music Festival 2026', 'Central Park, New York', NOW() + INTERVAL '30 days', NOW() + INTERVAL '30 days 8 hours', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768364031/6a7377d9-2f58-45d0-a186-c3ba9ee8ce83.png'),
('Tech Innovation Summit', 'Silicon Valley Convention Center', NOW() + INTERVAL '45 days', NOW() + INTERVAL '47 days', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363998/657b9588-d1e9-4eb5-8247-0e2d18f8cf40.png'),
('International Food Festival', 'Downtown Market Square', NOW() + INTERVAL '60 days', NOW() + INTERVAL '60 days 10 hours', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363984/d5b57b18-19ec-4f3b-9ba3-cbaf2d0bdb08.png'),
('Art & Design Expo 2026', 'Metropolitan Art Gallery', NOW() + INTERVAL '75 days', NOW() + INTERVAL '77 days', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363980/1ef7f3ee-3cf3-4375-b9db-77e12f677b88.png'),
('Sports Championship Finals', 'National Stadium', NOW() + INTERVAL '90 days', NOW() + INTERVAL '90 days 6 hours', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363932/a3cbf01d-77ca-4843-80be-1f3865a848b8.png'),
('Comedy Night Live Show', 'The Grand Theater', NOW() + INTERVAL '105 days', NOW() + INTERVAL '105 days 3 hours', 'https://res.cloudinary.com/dmfvnmpuq/image/upload/v1768363930/70ee4961-ed31-4691-b623-91348b9fb6e0.png')
ON CONFLICT (title) DO NOTHING;

-- 3. Ticket Types for each event
-- We use subqueries to get the correct event IDs

-- Summer Music Festival 2026
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'VIP Lounge', id, 3750000.0, 50, 5 FROM events WHERE title = 'Summer Music Festival 2026';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Fan Zone', id, 1875000.0, 200, 90 FROM events WHERE title = 'Summer Music Festival 2026';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'General Admission', id, 1250000.0, 1000, 80 FROM events WHERE title = 'Summer Music Festival 2026';

-- Tech Innovation Summit
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Investor Pass', id, 12500000.0, 50, 10 FROM events WHERE title = 'Tech Innovation Summit';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Developer Pass', id, 6250000.0, 300, 10 FROM events WHERE title = 'Tech Innovation Summit';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Student Pass', id, 5000000.0, 100, 10 FROM events WHERE title = 'Tech Innovation Summit';

-- International Food Festival
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Full Experience', id, 2000000.0, 100, 10 FROM events WHERE title = 'International Food Festival';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Tasting Pass', id, 1125000.0, 300, 30 FROM events WHERE title = 'International Food Festival';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Entry Only', id, 875000.0, 500, 50 FROM events WHERE title = 'International Food Festival';

-- Art & Design Expo 2026
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Collector Preview', id, 3000000.0, 30, 1 FROM events WHERE title = 'Art & Design Expo 2026';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Workshop Bundle', id, 1625000.0, 50, 50 FROM events WHERE title = 'Art & Design Expo 2026';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Gallery Access', id, 1250000.0, 400, 40 FROM events WHERE title = 'Art & Design Expo 2026';

-- Sports Championship Finals
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Luxury Box', id, 5000000.0, 20, 20 FROM events WHERE title = 'Sports Championship Finals';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Front Row', id, 3000000.0, 100, 10 FROM events WHERE title = 'Sports Championship Finals';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Side Stand', id, 2250000.0, 2000, 20 FROM events WHERE title = 'Sports Championship Finals';

-- Comedy Night Live Show
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Front Table', id, 2500000.0, 20, 20 FROM events WHERE title = 'Comedy Night Live Show';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Standard Seat', id, 1500000.0, 150, 15 FROM events WHERE title = 'Comedy Night Live Show';
INSERT INTO ticket_types (name, event_id, price, initial_quantity, remaining_quantity)
SELECT 'Balcony', id, 1125000.0, 100, 10 FROM events WHERE title = 'Comedy Night Live Show';
