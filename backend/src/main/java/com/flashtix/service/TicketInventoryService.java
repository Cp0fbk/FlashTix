package com.flashtix.service;

import java.util.List;

public interface TicketInventoryService {
    void initTicketStock(Long ticketTypeId);

    void decreaseStock(Long ticketTypeId, int quantity);

    void revertStock(Long ticketTypeId, int quantity);

    /**
     * Pre-warm stock cache for specific ticket types
     * 
     * @param ticketTypeIds List of ticket type IDs to warm up
     * @return Number of successfully warmed ticket types
     */
    int warmUpStockCache(List<Long> ticketTypeIds);
}
