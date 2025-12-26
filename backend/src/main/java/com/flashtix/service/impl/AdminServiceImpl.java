package com.flashtix.service.impl;

import com.flashtix.common.enums.ErrorCode;
import com.flashtix.common.exceptions.AppException;
import com.flashtix.dto.CreateEventRequest;
import com.flashtix.dto.CreateEventRequest.TicketRequest;
import com.flashtix.entity.Event;
import com.flashtix.entity.TicketType;
import com.flashtix.repository.EventRepository;
import com.flashtix.service.AdminService;
import com.flashtix.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final EventRepository eventRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public void createEvent(CreateEventRequest request, MultipartFile bannerImage) {
        log.info("Creating new event: {}", request.title());

        // Check for duplicate event title
        if (eventRepository.existsByTitle(request.title())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE, "Event with the same title already exists");
        }

        // Validate that at least one ticket type is provided
        if (request.ticketTypes() == null || request.ticketTypes().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "At least one ticket type is required");
        }

        // Validate time range
        validateTimeRange(request.startTime(), request.endTime());

        // Store banner image if provided
        String bannerUrl = null;
        if (bannerImage != null && !bannerImage.isEmpty()) {
            bannerUrl = fileStorageService.storeFile(bannerImage);
        }

        // Create event entity
        Event event = Event.builder()
                .title(request.title())
                .location(request.location())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .bannerUrl(bannerUrl)
                .build();

        // Create ticket types and associate with event
        List<TicketType> ticketTypes = createTicketTypes(request.ticketTypes(), event);
        event.setTicketTypes(ticketTypes);

        // Save event (ticket types will be saved due to cascade)
        eventRepository.save(event);

        log.info("Event '{}' created successfully with ID: {} and {} ticket types",
                event.getTitle(), event.getId(), ticketTypes.size());
    }

    private List<TicketType> createTicketTypes(List<TicketRequest> ticketRequests, Event event) {
        List<TicketType> ticketTypes = new ArrayList<>();

        for (TicketRequest ticketRequest : ticketRequests) {
            // Validate each ticket
            validateTicket(ticketRequest);

            TicketType ticketType = TicketType.builder()
                    .name(ticketRequest.name())
                    .price(ticketRequest.price())
                    .initialQuantity(ticketRequest.quantity())
                    .remainingQuantity(ticketRequest.quantity())
                    .event(event)
                    .build();
            ticketTypes.add(ticketType);
        }

        return ticketTypes;
    }

    private void validateTicket(TicketRequest ticket) {
        if (ticket.name() == null || ticket.name().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Ticket name is required");
        }
        if (ticket.price() == null || ticket.price() < 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                "Ticket price for '" + ticket.name() + "' must be a non-negative number");
        }
        if (ticket.quantity() == null || ticket.quantity() < 1) {
            throw new AppException(ErrorCode.INVALID_REQUEST,
                "Ticket quantity for '" + ticket.name() + "' must be at least 1");
        }
    }

    private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (endTime.isBefore(startTime)) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "End time must be after start time");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Start time must be in the future");
        }
    }
}
