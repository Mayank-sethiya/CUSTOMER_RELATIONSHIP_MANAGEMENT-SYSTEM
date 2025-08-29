package com.CustomerRelationshipManagement.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class FileStorageService {

    @Value("${local.storage.path}")
    private String storagePath;

    // Save array of files locally and return just the file names
    public List<String> saveFiles(MultipartFile[] files) throws IOException {
        List<String> fileNames = new ArrayList<>();

        Path dir = Paths.get(storagePath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        for (MultipartFile file : files) {
            String originalName = Objects.requireNonNull(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID() + "_" + originalName;

            Path filePath = dir.resolve(uniqueFileName);
            Files.write(filePath, file.getBytes());

            // ✅ Store only file name, not full file path
            fileNames.add(uniqueFileName);
        }

        return fileNames;
    }
}
