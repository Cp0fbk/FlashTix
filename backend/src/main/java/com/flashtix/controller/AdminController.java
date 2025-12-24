package com.flashtix.controller;

import com.flashtix.common.dto.ApiResponse;
import com.flashtix.dto.CreateEventRequest;
import com.flashtix.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Controller", description = "APIs for admin operations")
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Create a new event by admin",
        description = "Create a new event with title, location, time, and optional banner image. Requires X-Admin-API-Key header."
    )
    public ResponseEntity<ApiResponse<Void>> createEvent(
            @Valid @ModelAttribute CreateEventRequest request
    ) {

        adminService.createEvent(request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("Event created successfully")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
