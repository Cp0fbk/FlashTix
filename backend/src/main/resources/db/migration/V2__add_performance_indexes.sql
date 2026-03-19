-- Orders Table Indexes
-- =====================================================

-- CRITICAL: Order code lookups (used in payment callbacks)
CREATE INDEX IF NOT EXISTS idx_orders_order_code ON orders(order_code);

-- CRITICAL: Booking code lookups (used for ticket validation)
CREATE INDEX IF NOT EXISTS idx_orders_booking_code ON orders(booking_code);

-- HIGH: Expired orders cleanup job (runs every minute)
CREATE INDEX IF NOT EXISTS idx_orders_status_expired ON orders(status, expired_at);

-- MEDIUM: Customer email lookups
CREATE INDEX IF NOT EXISTS idx_orders_customer_email ON orders(customer_email);


-- Tickets Table Indexes
-- =====================================================

-- CRITICAL: Ticket code lookups (used for check-in)
CREATE INDEX IF NOT EXISTS idx_tickets_ticket_code ON tickets(ticket_code);

-- HIGH: Order tickets lookup (1-N relationship)
CREATE INDEX IF NOT EXISTS idx_tickets_order_id ON tickets(order_id);

-- MEDIUM: Ticket status queries (for analytics)
CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);

-- MEDIUM: Check-in time queries (for analytics)
CREATE INDEX IF NOT EXISTS idx_tickets_checked_in_at ON tickets(checked_in_at) WHERE checked_in_at IS NOT NULL;


-- Events Table Indexes
-- =====================================================

-- HIGH: Event listing by time (homepage, upcoming events)
CREATE INDEX IF NOT EXISTS idx_events_start_time ON events(start_time);

-- MEDIUM: Event search by title
CREATE INDEX IF NOT EXISTS idx_events_title ON events(title);

-- MEDIUM: Active events query
CREATE INDEX IF NOT EXISTS idx_events_active ON events(start_time, end_time);


-- Ticket Types Table Indexes
-- =====================================================

-- HIGH: Ticket types by event (1-N relationship)
CREATE INDEX IF NOT EXISTS idx_ticket_types_event_id ON ticket_types(event_id);

-- MEDIUM: Available tickets query
CREATE INDEX IF NOT EXISTS idx_ticket_types_remaining ON ticket_types(remaining_quantity) WHERE remaining_quantity > 0;


-- Payments Table Indexes
-- =====================================================

-- HIGH: Payment status lookups
CREATE INDEX IF NOT EXISTS idx_payments_transaction_code ON payments(transaction_code);

-- MEDIUM: Order payments (1-N relationship)
CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);

-- MEDIUM: Payment status analytics
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);


-- =====================================================
-- Verification Queries
-- =====================================================
-- Run these to verify indexes were created successfully

-- \di idx_orders_*
-- \di idx_tickets_*
-- \di idx_events_*
-- \di idx_ticket_types_*
-- \di idx_payments_*


-- =====================================================
-- Performance Testing
-- =====================================================
-- Before/After index creation comparison:

-- Test 1: Order lookup by booking code
-- EXPLAIN ANALYZE SELECT * FROM orders WHERE booking_code = 'FT-ABC123';

-- Test 2: Expired orders cleanup
-- EXPLAIN ANALYZE SELECT * FROM orders WHERE status = 'PENDING' AND expired_at < NOW();

-- Test 3: Ticket lookup by code
-- EXPLAIN ANALYZE SELECT * FROM tickets WHERE ticket_code = 'TK-1234567890';

-- Test 4: Event listing
-- EXPLAIN ANALYZE SELECT * FROM events WHERE start_time > NOW() ORDER BY start_time LIMIT 10;


-- =====================================================
-- Expected Performance Gains
-- =====================================================
-- Without indexes:
--   - Sequential scan on 100K orders: 50-100ms
--   - Sequential scan on 1M tickets: 500ms-1s
--
-- With indexes:
--   - Index scan: 0.5-1ms
--   - Improvement: 100-1000x faster
-- =====================================================
