package com.flashtix.service;

import org.springframework.data.domain.Pageable;

import com.flashtix.common.dto.PageResponse;
import com.flashtix.dto.response.EventResponse;

public interface UserService {

    PageResponse<EventResponse> getTickets(Pageable pageable);
}
