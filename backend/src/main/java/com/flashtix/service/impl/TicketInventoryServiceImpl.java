package com.flashtix.service.impl;

import com.flashtix.common.exceptions.AppException;
import com.flashtix.common.enums.ErrorCode;
import com.flashtix.entity.TicketType;
import com.flashtix.repository.TicketTypeRepository;
import com.flashtix.service.TicketInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketInventoryServiceImpl implements TicketInventoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TicketTypeRepository ticketTypeRepository;

    private static final String STOCK_KEY_PREFIX = "ticket_stock:";

    // Lua scripts for atomic operations
    private DefaultRedisScript<Long> decreaseStockScript;
    private DefaultRedisScript<Long> revertStockScript;

    @PostConstruct
    public void init() {
        // Load decrease stock Lua script
        decreaseStockScript = new DefaultRedisScript<>();
        decreaseStockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/decrease_stock.lua")));
        decreaseStockScript.setResultType(Long.class);

        // Load revert stock Lua script
        revertStockScript = new DefaultRedisScript<>();
        revertStockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/revert_stock.lua")));
        revertStockScript.setResultType(Long.class);

        log.info("Loaded Redis Lua scripts for stock management");
    }

    /**
     * Pre-warm Redis cache with ticket stocks
     * Should be called manually via Admin API, not on startup
     * Uses setIfAbsent to avoid N+1 problem
     */
    public int warmUpStockCache(List<Long> ticketTypeIds) {
        int successCount = 0;

        for (Long ticketTypeId : ticketTypeIds) {
            try {
                TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                        .orElseThrow(() -> new RuntimeException("Ticket Type not found: " + ticketTypeId));

                String key = STOCK_KEY_PREFIX + ticketTypeId;

                // OPTIMIZATION: setIfAbsent is atomic - no need for hasKey check
                Boolean wasSet = redisTemplate.opsForValue()
                        .setIfAbsent(key, String.valueOf(ticketType.getRemainingQuantity()));

                if (Boolean.TRUE.equals(wasSet)) {
                    successCount++;
                    log.debug("Initialized stock cache for ticket type {}", ticketTypeId);
                } else {
                    log.debug("Stock already cached for ticket type {}", ticketTypeId);
                }
            } catch (Exception e) {
                log.error("Failed to warm up stock for ticket type {}", ticketTypeId, e);
            }
        }

        log.info("✅ Stock cache warmup completed: {}/{} ticket types", successCount, ticketTypeIds.size());
        return successCount;
    }

    @Override
    public void initTicketStock(Long ticketTypeId) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return;
        }

        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId)
                .orElseThrow(() -> new RuntimeException("Ticket Type not found"));

        redisTemplate.opsForValue().set(key, String.valueOf(ticketType.getRemainingQuantity()));
        log.info("Initialized stock for ticket type {} : {}", ticketTypeId, ticketType.getRemainingQuantity());
    }

    @Override
    public void decreaseStock(Long ticketTypeId, int quantity) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;

        // PERFORMANCE OPTIMIZATION: Atomic decrease using Lua script
        // Eliminates race condition between check and decrement
        Long result = redisTemplate.execute(
                decreaseStockScript,
                Collections.singletonList(key),
                String.valueOf(quantity));

        if (result == null) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Redis script execution failed");
        }

        // Handle script return codes
        if (result == -1) {
            // Stock not initialized, initialize and retry
            initTicketStock(ticketTypeId);
            result = redisTemplate.execute(
                    decreaseStockScript,
                    Collections.singletonList(key),
                    String.valueOf(quantity));
        }

        if (result != null && result == -2) {
            throw new AppException(ErrorCode.OUT_OF_STOCK, "Ticket is sold out for type: " + ticketTypeId);
        }

        log.debug("Decreased stock for ticket type {}, new stock: {}", ticketTypeId, result);
    }

    @Override
    public void revertStock(Long ticketTypeId, int quantity) {
        String key = STOCK_KEY_PREFIX + ticketTypeId;

        // PERFORMANCE OPTIMIZATION: Use Lua script for atomic revert
        Long result = redisTemplate.execute(
                revertStockScript,
                Collections.singletonList(key),
                String.valueOf(quantity));

        if (result == null || result == -1) {
            // If stock not in cache, sync from database
            log.warn("Stock key not found in Redis for ticket type {}, re-initializing", ticketTypeId);
            initTicketStock(ticketTypeId);

            // Retry revert with initialized stock
            result = redisTemplate.execute(
                    revertStockScript,
                    Collections.singletonList(key),
                    String.valueOf(quantity));
        }

        log.info("Reverted stock for ticket type {}, new stock: {}", ticketTypeId, result);
    }
}
