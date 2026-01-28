package com.flashtix.service;

import org.springframework.web.multipart.MultipartFile;

import com.flashtix.dto.request.CreateEventRequest;

public interface AdminService {
    void createEvent(CreateEventRequest event, MultipartFile bannerImage);
}
