package com.flashtix.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        String title,
        LocalDateTime startTime,
        String location,
        String bannerUrl,
        List<TicketResponse> tickets) {

    public record TicketResponse(
            String name,
            double price,
            int remainingQuantity) {
    }
}
