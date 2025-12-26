package com.flashtix.service;

import com.flashtix.dto.CreateEventRequest;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    void createEvent(CreateEventRequest event, MultipartFile bannerImage);
}
