package com.flashtix.service;

import org.springframework.data.domain.Pageable;

import com.flashtix.dto.EventResponse;
import com.flashtix.common.dto.PageResponse;

public interface UserService {

    PageResponse<EventResponse> getTickets(Pageable pageable);
}
