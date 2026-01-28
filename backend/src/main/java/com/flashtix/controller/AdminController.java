package com.flashtix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flashtix.common.dto.ApiResponse;
import com.flashtix.dto.request.CreateEventRequest;
import com.flashtix.service.AdminService;
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

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "APIs for admin operations")
public class AdminController {

    private final AdminService adminService;

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
}
