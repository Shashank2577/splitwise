package com.splitclone.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.UUID;

// @Service
public class FirebaseStorageService {

    // @Value("${firebase.storage.bucket}")
    private String bucketName;

    private Storage storage;

    // @PostConstruct
    public void initialize() {
        try {
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebase-service-account.json").getInputStream()
            );
            storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase Storage", e);
        }
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = folder + "/" + UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            Blob blob = storage.create(blobInfo, file.getInputStream());
            return blob.getMediaLink();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        return uploadFile(file, "default");
    }

    public void deleteFile(String fileUrl) {
        try {
            // Extract file name from URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
            BlobId blobId = BlobId.of(bucketName, fileName);
            storage.delete(blobId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
