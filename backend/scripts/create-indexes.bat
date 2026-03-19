@echo off
REM Run indexes via Docker PostgreSQL container
echo Creating performance indexes via Docker...

docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_orders_order_code ON orders(order_code);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_orders_booking_code ON orders(booking_code);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_orders_status_expired ON orders(status, expired_at);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_orders_customer_email ON orders(customer_email);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_tickets_ticket_code ON tickets(ticket_code);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_tickets_order_id ON tickets(order_id);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_tickets_status ON tickets(status);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_events_start_time ON events(start_time);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_events_title ON events(title);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_ticket_types_event_id ON ticket_types(event_id);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_payments_transaction_id ON payments(transaction_id);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_payments_order_id ON payments(order_id);"
docker exec -i flashtix-postgres psql -U postgres -d flashtix -c "CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);"

echo.
echo Indexes created successfully!
echo Run this to verify: docker exec -it flashtix-postgres psql -U postgres -d flashtix -c "\di idx_*"
