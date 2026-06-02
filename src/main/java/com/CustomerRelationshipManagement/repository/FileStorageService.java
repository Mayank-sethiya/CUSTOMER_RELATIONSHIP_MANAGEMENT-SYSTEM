package com.CustomerRelationshipManagement.repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileStorageService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads multiple files to Cloudinary and returns their secure URLs.
     * This replaces the Azure Blob logic.
     */
    public List<String> saveFiles(MultipartFile[] files) throws IOException {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Upload to Cloudinary with 'auto' resource type (handles images, PDFs, etc.)
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                        ObjectUtils.asMap("resource_type", "auto"));
                
                // Get the permanent secure URL (HTTPS)
                String publicUrl = uploadResult.get("secure_url").toString();
                fileUrls.add(publicUrl);
            }
        }

        return fileUrls;
    }

    /**
     * Since Cloudinary returns the full URL immediately after upload,
     * this method simply returns the fileName if it's already a URL.
     */
    public String getFileUrl(String fileName) {
        return fileName;
    }
}
