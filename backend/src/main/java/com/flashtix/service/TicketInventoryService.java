package com.flashtix.service;

public interface TicketInventoryService {
    void initTicketStock(Long ticketTypeId);

    void decreaseStock(Long ticketTypeId, int quantity);

    void revertStock(Long ticketTypeId, int quantity);
}
