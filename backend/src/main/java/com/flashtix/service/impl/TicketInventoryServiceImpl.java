package com.flashtix.service.impl;

import com.flashtix.common.exceptions.AppException;
import com.flashtix.common.enums.ErrorCode;
import com.flashtix.entity.TicketType;
import com.flashtix.repository.TicketTypeRepository;
import com.flashtix.service.TicketInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketInventoryServiceImpl implements TicketInventoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TicketTypeRepository ticketTypeRepository;

    private static final String STOCK_KEY_PREFIX = "ticket_stock:";

    @Override
    public void initTicketStock(Long ticketTypeId) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return;
        }

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new RuntimeException("Ticket Type not found"));

        redisTemplate.opsForValue().set(key, String.valueOf(ticketType.getRemainingQuantity()));
        // Optional: Set expiration if needed, but stock usually persists until event
        // redisTemplate.expire(key, 24, TimeUnit.HOURS);
        log.info("Initialized stock for ticket type {} : {}", ticketTypeId, ticketType.getRemainingQuantity());
    }

    @Override
    public void decreaseStock(Long ticketTypeId, int quantity) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;

        // Ensure stock is initialized (Lazy loading strategy if not pre-warmed)
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            initTicketStock(ticketTypeId);
        }

        Long newStock = redisTemplate.opsForValue().decrement(key, quantity);

        if (newStock != null && newStock < 0) {
            // Revert changes immediately
            redisTemplate.opsForValue().increment(key, quantity);
            throw new AppException(ErrorCode.OUT_OF_STOCK, "Ticket is sold out for type: " + ticketTypeId);
        }
    }

    @Override
    public void revertStock(Long ticketTypeId, int quantity) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;
        redisTemplate.opsForValue().increment(key, quantity);
        log.info("Reverted stock for ticket type {}, quantity: {}", ticketTypeId, quantity);
    }
}
