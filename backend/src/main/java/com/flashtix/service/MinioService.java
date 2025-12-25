package com.flashtix.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    /**
     * Upload file to MinIO and return the file URL
     */
    String uploadFile(MultipartFile file);

    /**
     * Delete file from MinIO
     */
    void deleteFile(String fileName);

    /**
     * Get file URL from MinIO
     */
    String getFileUrl(String fileName);
}

