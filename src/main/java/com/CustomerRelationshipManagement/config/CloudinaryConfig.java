package com.CustomerRelationshipManagement.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        // This will automatically pick up the CLOUDINARY_URL variable you added to Render
        return new Cloudinary();
    }
}
