package com.flashtix.controller;

import com.flashtix.common.dto.ApiResponse;
import com.flashtix.dto.request.CheckInRequest;
import com.flashtix.dto.response.CheckInResponse;
import com.flashtix.dto.response.OrderDetailResponse;
import com.flashtix.dto.response.TicketResponse;
import com.flashtix.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Controller", description = "Ticket validation and check-in APIs")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/booking/{bookingCode}")
    @Operation(summary = "Get order details by booking code", description = "Retrieve order information and all associated tickets using the booking code")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderByBookingCode(
            @PathVariable String bookingCode) {
        try {
            OrderDetailResponse response = ticketService.getOrderByBookingCode(bookingCode);
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), "Order retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/check-in")
    @Operation(summary = "Check in tickets", description = "Check in tickets at event entrance. Can check in all tickets or specific tickets by providing ticket codes.")
    public ResponseEntity<ApiResponse<CheckInResponse>> checkInTickets(
            @RequestBody CheckInRequest request) {
        try {
            CheckInResponse response = ticketService.checkInTickets(request);
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), "Tickets checked in successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/{ticketCode}")
    @Operation(summary = "Get ticket by code", description = "Retrieve individual ticket details by ticket code")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketByCode(
            @PathVariable String ticketCode) {
        try {
            TicketResponse response = ticketService.getTicketByCode(ticketCode);
            return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.OK.value(), "Ticket retrieved successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }
}
