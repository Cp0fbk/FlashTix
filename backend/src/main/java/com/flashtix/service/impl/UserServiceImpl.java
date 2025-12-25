package com.flashtix.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flashtix.common.dto.PageResponse;
import com.flashtix.dto.EventResponse;
import com.flashtix.dto.EventResponse.TicketResponse;
import com.flashtix.entity.Event;
import com.flashtix.repository.EventRepository;
import com.flashtix.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final EventRepository eventRepository;

    @Override
    public PageResponse<EventResponse> getTickets(Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAllWithTicketTypes(pageable);

        Page<EventResponse> responsePage = eventPage.map(event -> {
            // Map all ticket types to TicketResponse
            List<TicketResponse> ticketResponses = null;
            if (event.getTicketTypes() != null && !event.getTicketTypes().isEmpty()) {
                ticketResponses = event.getTicketTypes().stream()
                        .map(ticket -> new TicketResponse(
                                ticket.getName(),
                                ticket.getPrice(),
                                ticket.getRemainingQuantity()))
                        .toList();
            }

            return new EventResponse(
                    event.getTitle(),
                    event.getStartTime(),
                    event.getLocation(),
                    ticketResponses);
        });

        return PageResponse.of(responsePage);
    }
}
