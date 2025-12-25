package com.flashtix.service.impl;

import com.flashtix.service.FileStorageService;
import com.flashtix.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioService minioService;

    @Override
    public String storeFile(MultipartFile file) {
        return minioService.uploadFile(file);
    }
}

