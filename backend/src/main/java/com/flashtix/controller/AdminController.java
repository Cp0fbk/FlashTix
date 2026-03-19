package com.flashtix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flashtix.common.dto.ApiResponse;
import com.flashtix.dto.request.CreateEventRequest;
import com.flashtix.service.AdminService;
import com.flashtix.service.TicketInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "APIs for admin operations")
public class AdminController {

    private final AdminService adminService;
    private final TicketInventoryService ticketInventoryService;

    @PostMapping(value = "/event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new event by admin", description = "Create a new event with title, location, time, ticket types, and optional banner image. ")
    public ResponseEntity<ApiResponse<Void>> createEvent(
            @Parameter(schema = @Schema(implementation = CreateEventRequest.class)) @RequestPart("event") String eventJson,
            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            CreateEventRequest event = objectMapper.readValue(eventJson, CreateEventRequest.class);
            adminService.createEvent(event, bannerImage);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(HttpStatus.CREATED.value())
                    .message("Event created successfully")
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            throw new RuntimeException("Format JSON error: " + e.getMessage(), e);
        }
    }

    @PostMapping("/stock/warmup")
    @Operation(summary = "Pre-warm stock cache for specific ticket types", description = "Manually pre-warm Redis cache with stock data for specified ticket types. "
            +
            "Uses atomic setIfAbsent to avoid overwriting existing cache. " +
            "Should be called before flash sales for optimal performance.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> warmUpStockCache(
            @RequestBody List<Long> ticketTypeIds) {

        int successCount = ticketInventoryService.warmUpStockCache(ticketTypeIds);

        Map<String, Object> result = Map.of(
                "requested", ticketTypeIds.size(),
                "warmed", successCount,
                "message", String.format("Successfully warmed %d/%d ticket types", successCount, ticketTypeIds.size()));

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Stock warmup completed", result));
    }
}
