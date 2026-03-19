-- Atomic stock revert with validation
-- KEYS[1]: Redis key for stock (ticket_stock:123)
-- ARGV[1]: Quantity to increment (revert)

-- Use GET instead of EXISTS to be truly atomic
local current = redis.call('GET', KEYS[1])

-- If key doesn't exist, return -1 (not initialized)
if not current then
    return -1
end

-- INCRBY is atomic - no need for separate calculation
return redis.call('INCRBY', KEYS[1], ARGV[1])
