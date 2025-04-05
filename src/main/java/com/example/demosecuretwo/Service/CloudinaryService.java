package com.example.demosecuretwo.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(@Value("${cloudinary.cloud-name}") String cloudName,
                             @Value("${cloudinary.api-key}") String apiKey,
                             @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    // Upload video and return its Cloudinary URL + public_id
    public Map<String, String> uploadVideo(byte[] fileBytes) throws Exception {
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "videos",
                "access_mode", "authenticated"
        ));

        return Map.of(
                "secure_url", uploadResult.get("secure_url").toString(),
                "public_id", uploadResult.get("public_id").toString()
        );
    }

    public Map<String, String> uploadImage(byte[] fileBytes) throws IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("folder", "images");

        Map uploadResult = cloudinary.uploader().upload(fileBytes, options);

        return Map.of(
                "public_id", (String) uploadResult.get("public_id"),
                "secure_url", (String) uploadResult.get("secure_url") // Include full image URL
        );
    }


    // Generate a signed URL with expiration for secure access
    public String generateSignedUrl(String publicId) {
        long expirationTimestamp = (System.currentTimeMillis() / 1000L + (10 * 365 * 24 * 60 * 60)); // 10 years expiration) + expirationSeconds; // Convert to UNIX timestamp
        return cloudinary.url()
                .secure(true)//
                .resourceType("video")
                .publicId(publicId)
                .signed(true)
                .generate() + "?exp=" + expirationTimestamp; // Attach expiration time manually
    }

    // Delete video from Cloudinary using public ID
    public void deleteVideo(String publicId) throws Exception {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                "resource_type", "video"
        ));
    }
}
