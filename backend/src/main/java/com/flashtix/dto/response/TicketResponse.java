package com.flashtix.dto.response;

import com.flashtix.common.enums.TicketStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TicketResponse(
        Long id,
        String ticketCode,
        TicketStatus status,
        LocalDateTime createdAt,
        LocalDateTime checkedInAt,
        String checkedInBy) {
}
