package com.flashtix.service;

import com.flashtix.dto.request.CheckInRequest;
import com.flashtix.dto.response.CheckInResponse;
import com.flashtix.dto.response.OrderDetailResponse;
import com.flashtix.dto.response.TicketResponse;

public interface TicketService {

    /**
     * Retrieve order details by booking code
     */
    OrderDetailResponse getOrderByBookingCode(String bookingCode);

    /**
     * Check in tickets at event entrance
     */
    CheckInResponse checkInTickets(CheckInRequest request);

    /**
     * Get individual ticket details
     */
    TicketResponse getTicketByCode(String ticketCode);
}
