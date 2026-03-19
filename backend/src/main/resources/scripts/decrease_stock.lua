-- Atomic stock decrease with validation
-- KEYS[1]: Redis key for stock (ticket_stock:123)
-- ARGV[1]: Quantity to decrease

local current = redis.call('GET', KEYS[1])

-- If key doesn't exist, return -1 (not initialized)
if not current then
    return -1
end

local stock = tonumber(current)
local quantity = tonumber(ARGV[1])

-- Check if sufficient stock
if stock < quantity then
    return -2  -- Insufficient stock
end

-- Decrease stock and return new value
return redis.call('DECRBY', KEYS[1], quantity)
