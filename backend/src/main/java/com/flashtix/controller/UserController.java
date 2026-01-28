package com.flashtix.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flashtix.common.dto.ApiResponse;
import com.flashtix.common.dto.PageResponse;
import com.flashtix.dto.response.EventResponse;
import com.flashtix.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/events")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User apis with ticket")
public class UserController {
        private final UserService userService;

        @GetMapping("/tickets")
        @Operation(summary = "Get all ticket")
        public ResponseEntity<ApiResponse<PageResponse<EventResponse>>> getTickets(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "ASC") String sortDir,
                        @RequestParam(defaultValue = "title") String sortBy) {

                PageResponse<EventResponse> tickets = userService
                                .getTickets(PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortBy));
                return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Tickets fetched successfully",
                                tickets));
        }
}