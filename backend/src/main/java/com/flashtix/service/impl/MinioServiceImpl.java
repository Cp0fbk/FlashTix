package com.flashtix.service.impl;

import com.flashtix.common.enums.ErrorCode;
import com.flashtix.common.exceptions.AppException;
import com.flashtix.service.MinioService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @PostConstruct
    public void init() {
        try {
            // Check if bucket exists, if not create it
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("MinIO bucket '{}' created successfully", bucketName);

                // Set bucket policy to public read
                String policy = """
                        {
                            "Version": "2012-10-17",
                            "Statement": [
                                {
                                    "Effect": "Allow",
                                    "Principal": {"AWS": "*"},
                                    "Action": ["s3:GetObject"],
                                    "Resource": ["arn:aws:s3:::%s/*"]
                                }
                            ]
                        }
                        """.formatted(bucketName);

                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucketName)
                                .config(policy)
                                .build()
                );
                log.info("MinIO bucket '{}' policy set to public read", bucketName);
            } else {
                log.info("MinIO bucket '{}' already exists", bucketName);
            }
        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Failed to initialize MinIO bucket: " + e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID() + extension;

            // Upload file to MinIO
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            log.info("File uploaded successfully to MinIO: {}", fileName);

            // Return the file URL
            return getFileUrl(fileName);

        } catch (IOException | ErrorResponseException | InsufficientDataException |
                 InternalException | InvalidKeyException | InvalidResponseException |
                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Failed to upload file to MinIO: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        try {
            // Extract filename from URL if it's a full URL
            if (fileName.startsWith("http")) {
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            }

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

            log.info("File deleted successfully from MinIO: {}", fileName);

        } catch (ErrorResponseException | InsufficientDataException | InternalException |
                 InvalidKeyException | InvalidResponseException | IOException |
                 NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR,
                    "Failed to delete file from MinIO: " + e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String fileName) {
        // Return the public URL for the file
        return String.format("%s/%s/%s", endpoint, bucketName, fileName);
    }
}

