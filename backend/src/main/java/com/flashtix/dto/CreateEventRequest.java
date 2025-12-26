package com.flashtix.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record CreateEventRequest (
    String title,
    String location,
    LocalDateTime startTime,
    LocalDateTime endTime,

    @NotBlank(message = "Ticket types is required")
    List<TicketRequest> ticketTypes

){
    public record TicketRequest (
        @NotBlank(message = "Ticket type name is required")
        String name,
        Double price,
        Integer quantity
    ){}
}


