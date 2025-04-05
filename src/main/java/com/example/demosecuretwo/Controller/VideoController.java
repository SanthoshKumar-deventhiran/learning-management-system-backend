package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.Model.Video;
import com.example.demosecuretwo.Repo.VideoRepo;
import com.example.demosecuretwo.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoRepo videoRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/{videoId}")
    public ResponseEntity<String> getVideoById(@PathVariable Integer videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Video ID must not be null");
        }

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        String publicId = video.getVideoUrl(); // Stored public_id in DB
        String signedUrl = cloudinaryService.generateSignedUrl(publicId); // Generate signed URL

        return ResponseEntity.ok(signedUrl);
    }
}
