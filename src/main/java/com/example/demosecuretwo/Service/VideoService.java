package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Model.Courses;
import com.example.demosecuretwo.Model.Video;
import com.example.demosecuretwo.Repo.CourseRepo;
import com.example.demosecuretwo.Repo.VideoRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class VideoService {

    private final CloudinaryService cloudinaryService;
    private final VideoRepo videoRepository;
    private final CourseRepo courseRepository;


    public VideoService(CloudinaryService cloudinaryService, VideoRepo videoRepository, CourseRepo courseRepository) {
        this.cloudinaryService = cloudinaryService;
        this.videoRepository = videoRepository;
        this.courseRepository = courseRepository;
    }

    // Upload videos and generate signed URLs
    public Video uploadVideo(Integer courseId, MultipartFile file, String title, Boolean isfree) throws Exception {
        // Retrieve the course from the database
        Courses course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));


        // Upload video to Cloudinary and get the result map
        Map<String, String> uploadResult = cloudinaryService.uploadVideo(file.getBytes());

        // Extract the public ID and generate the signed URL
        String publicId = uploadResult.get("public_id");
        String signedUrl = cloudinaryService.generateSignedUrl(publicId);

        // Save video details in the database
        Video video = new Video();
        video.setTitle(title);
        video.setVideoUrl(signedUrl);
        video.setIsFree(isfree);
        video.setCourses(course);
        video = videoRepository.save(video);


        return video;
    }

    public Video findById(Integer videoId){
        Video video = videoRepository.findById(videoId).orElseThrow(
                ()-> new ResourceNotFoundException("Video is not found with ID:"+videoId));
        return video;
    }

    public String deleteVideo(Integer videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with ID:"+videoId));

        String videoUrl = video.getVideoUrl();
        String publicId = extractPublicId(videoUrl); // Extract public ID from URL

        try {
            // Delete video from Cloudinary
            cloudinaryService.deleteVideo(publicId);

            // Delete video from database
            videoRepository.delete(video);

            return "Video deleted successfully!";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete video", e);
        }
    }

    // Extracts the public ID from a Cloudinary video URL
//    private String extractPublicId(String videoUrl) {
//        // Example URL: https://res.cloudinary.com/demo/video/upload/v1700000000/myfolder/video1.mp4
//        String[] parts = videoUrl.split("/");
//        String fileNameWithExtension = parts[parts.length - 1]; // Get 'video1.mp4'
//        return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(".")); // Extract 'video1'
//    }

    private String extractPublicId(String videoUrl) {
        // Remove query parameters if any
        String cleanUrl = videoUrl.split("\\?")[0];

        // Split by '/' to get parts of the URL
        String[] parts = cleanUrl.split("/");

        // Based on your URL structure, the public ID is likely composed of:
        // - The folder (e.g., "videos")
        // - The file identifier (e.g., "xtp1gdy9jind2raugdab")
        // You might need to adjust these indices if your URL structure differs.

        // Assuming the structure is:
        // [0] "https:"
        // [1] ""
        // [2] "res.cloudinary.com"
        // [3] "dhccsw2ra"
        // [4] "video"
        // [5] "upload"
        // [6] "s--Apv63_BQ--" (optional signature/transformation)
        // [7] "v1"
        // [8] "videos"
        // [9] "xtp1gdy9jind2raugdab"

        // Construct the public id (folder + "/" + file identifier)
        if (parts.length < 10) {
            throw new IllegalArgumentException("Unexpected URL format: " + videoUrl);
        }
        return parts[8] + "/" + parts[9];
    }

}

