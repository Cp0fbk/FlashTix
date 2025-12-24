package com.flashtix.service.impl;

import com.flashtix.common.enums.ErrorCode;
import com.flashtix.common.exceptions.AppException;
import com.flashtix.dto.CreateEventRequest;
import com.flashtix.entity.Event;
import com.flashtix.repository.EventRepository;
import com.flashtix.service.AdminService;
import com.flashtix.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final EventRepository eventRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public void createEvent(CreateEventRequest createEventRequest) {
        log.info("Creating new event: {}", createEventRequest.getTitle());

        // Check for duplicate event title
        if (eventRepository.existsByTitle(createEventRequest.getTitle())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE, "Event with the same title already exists");
        }

        // Parse date times
        LocalDateTime startTime = parseDateTime(createEventRequest.getStartTime());
        LocalDateTime endTime = parseDateTime(createEventRequest.getEndTime());

        // Validate time range
        if (endTime.isBefore(startTime)) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "End time must be after start time");
        }

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Start time must be in the future");
        }

        // Store banner image if provided
        String bannerUrl = null;
        if (createEventRequest.getBannerImage() != null && !createEventRequest.getBannerImage().isEmpty()) {
            bannerUrl = fileStorageService.storeFile(createEventRequest.getBannerImage());
        }

        // Create event entity
        Event event = Event.builder()
                .title(createEventRequest.getTitle())
                .location(createEventRequest.getLocation())
                .startTime(startTime)
                .endTime(endTime)
                .bannerUrl(bannerUrl)
                .build();

        // Save event
        eventRepository.save(event);

        log.info("Event created successfully with ID: {}", event.getId());
    }

    private void validateCreateEventRequest(CreateEventRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Event title is required");
        }

        if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Event location is required");
        }

        if (request.getStartTime() == null || request.getStartTime().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Event start time is required");
        }

        if (request.getEndTime() == null || request.getEndTime().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST, "Event end time is required");
        }
    }

    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            // Try ISO format first: 2025-12-25T10:00:00
            return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            try {
                // Try alternative format: 2025-12-25 10:00:00
                return LocalDateTime.parse(dateTimeString,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (DateTimeParseException ex) {
                throw new AppException(ErrorCode.INVALID_REQUEST,
                    "Invalid date time format. Expected format: yyyy-MM-ddTHH:mm:ss or yyyy-MM-dd HH:mm:ss");
            }
        }
    }
}
