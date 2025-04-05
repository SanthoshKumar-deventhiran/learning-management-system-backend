package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.DTO.CourseDTO;
import com.example.demosecuretwo.DTO.CourseStatsDTO;
import com.example.demosecuretwo.DTO.VideoDTO;
import com.example.demosecuretwo.Model.Video;
import com.example.demosecuretwo.Repo.CourseRepo;
import com.example.demosecuretwo.Repo.VideoRepo;
import com.example.demosecuretwo.Service.CourseService;
import com.example.demosecuretwo.Service.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    VideoService videoService;

    @Transactional
    @PostMapping(path = "/course/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseDTO> addCourseAndVideos(
            @RequestPart("course") String courseJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "videoFiles", required = false) MultipartFile[] videoFiles) {

        try {
            // Convert JSON string to CourseDTO
            ObjectMapper objectMapper = new ObjectMapper();
            CourseDTO courseDTO = objectMapper.readValue(courseJson, CourseDTO.class);

            // Attach image file
            courseDTO.setImageFile(imageFile);

            // Save course details
            CourseDTO savedCourse = courseService.addCourse(courseDTO);

            // Process video uploads
            if (videoFiles != null && courseDTO.getVideos() != null) {
                List<VideoDTO> videos = new ArrayList<>();

                if (videoFiles.length != courseDTO.getVideos().size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mismatch between video files and details.");
                }

                for (int i = 0; i < videoFiles.length; i++) {
                    VideoDTO videoDetail = courseDTO.getVideos().get(i);
                    Video video = videoService.uploadVideo(
                            savedCourse.getId(), videoFiles[i], videoDetail.getTitle(), videoDetail.getIsFree());

                    videos.add(new VideoDTO(video));
                }

                savedCourse.setVideos(videos);
            }

            return ResponseEntity.ok(savedCourse);

        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid course JSON format.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing the request.", e);
        }
    }


    @PutMapping(path = "/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseDTO> editCourse(
            @PathVariable Integer id,
            @RequestPart("course") String courseJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "videoFiles", required = false) MultipartFile[] videoFiles,
            @RequestParam(value = "deleteVideoIds", required = false) List<Integer> deleteVideoIds) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        CourseDTO courseDTO = objectMapper.readValue(courseJson, CourseDTO.class);

        CourseDTO updatedCourse = courseService.editCourse(id, courseDTO, imageFile, videoFiles, deleteVideoIds);
        return ResponseEntity.ok(updatedCourse);
    }

    // Remove a course by ID
    @DeleteMapping("/removeCourse/{courseId}")
    public ResponseEntity<String> removeCourse(@PathVariable Integer courseId) {
        courseService.removeCourse(courseId);
        return ResponseEntity.ok("Course with ID " + courseId + " removed successfully.");
    }

    // Delete a video by ID
    @DeleteMapping("video/delete/{videoId}")
    public ResponseEntity<String> DeleteVideosById(@PathVariable Integer videoId) {
        if (videoId == null) {
            throw new IllegalArgumentException("Lesson ID must not be null");
        }
        return ResponseEntity.ok(videoService.deleteVideo(videoId));
    }

    @GetMapping("/totalStudents")
    public ResponseEntity<Integer> getTotalStudents() {
        Integer totalStudents = courseService.getTotalStudents();
        return ResponseEntity.ok(totalStudents);
    }

    // Get students enrolled this month across all courses
    @GetMapping("/monthlyStudents")
    public ResponseEntity<Integer> getMonthlyEnrolledStudents() {
        Integer studentsThisMonth = courseService.getStudentsEnrolledThisMonth();
        return ResponseEntity.ok(studentsThisMonth);
    }

    //Get total revenue
    @GetMapping("/totalRevenue")
    public ResponseEntity<Double> getTotalRevenue(){
        Double totalRevenue = courseService.getTotalRevenue();
        return ResponseEntity.ok(totalRevenue);
    }

    // Get Monthly revenue
    @GetMapping("/monthlyRevenue")
    public ResponseEntity<Double> getMonthlyRevenue(){
        Double revenueThisMonth = courseService.getRevenueThisMonth();
        return ResponseEntity.ok(revenueThisMonth);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<CourseStatsDTO>> getCourseStatistics() {
        return ResponseEntity.ok(courseService.getCourseStatistics());
    }
}
