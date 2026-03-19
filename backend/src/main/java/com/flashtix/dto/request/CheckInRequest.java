package com.flashtix.dto.request;

import java.util.List;

public record CheckInRequest(
        String bookingCode,
        List<String> ticketCodes, // Optional: if null/empty, check in all active tickets
        String validatedBy // Optional: staff identifier
) {
}
