package com.CustomerRelationshipManagement.repository;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final BlobServiceClient blobServiceClient;

    @Value("${azure.blob.container-name}")
    private String containerName;

    // The BlobServiceClient bean is automatically injected here
    public FileStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public List<String> saveFiles(MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();

        // Get a reference to the container and create it if it doesn't exist
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }

        for (MultipartFile file : files) {
            String originalName = Objects.requireNonNull(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID() + "_" + originalName;

            // Get a reference to the blob
            BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);

            // Upload the file data
            blobClient.upload(file.getInputStream(), file.getSize(), true); // true overwrites if exists

            fileNames.add(uniqueFileName);
        }

        return fileNames;
    }

    // You will need a method to get the URL of a stored file
    public String getFileUrl(String fileName) {
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        return blobClient.getBlobUrl();
    }
}
