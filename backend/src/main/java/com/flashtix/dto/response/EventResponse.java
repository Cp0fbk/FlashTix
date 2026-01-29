package com.flashtix.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
                Long eventId,
                String title,
                LocalDateTime startTime,
                String location,
                String bannerUrl,
                List<TicketResponse> tickets) {

        public record TicketResponse(
                        Long ticketId,
                        String name,
                        double price,
                        int remainingQuantity,
                        int initialQuantity) {
        }
}
