package com.flashtix.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CheckInResponse(
        String bookingCode,
        Integer totalTickets,
        Integer checkedInCount,
        Integer previouslyCheckedIn,
        List<TicketResponse> tickets,
        String message) {
}
