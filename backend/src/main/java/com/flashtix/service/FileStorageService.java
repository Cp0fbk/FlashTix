package com.flashtix.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * Store a file and return its URL/path
     */
    String storeFile(MultipartFile file);
}

