package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.Config.AuthUtil;
import com.example.demosecuretwo.DTO.CourseDTO;
import com.example.demosecuretwo.Model.Courses;
import com.example.demosecuretwo.Model.Video;
import com.example.demosecuretwo.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CoursePurchaseService coursePurchaseService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Autowired
    private AuthUtil authUtil;

    // Get all courses
    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Get a specific course by ID
    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Integer courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("/{courseId}/purchase")
    public ResponseEntity<String> purchaseCourse(@PathVariable Integer courseId) throws Exception {
        Integer userId = authUtil.getCurrentUserId();
        Courses course = courseService.findById(courseId);

        // Check if the user has already purchased the course
        if (coursePurchaseService.hasUserPurchasedCourse(userId, course)) {
            return ResponseEntity.badRequest().body("Course already purchased.");
        }

        // Process the payment (simulate)
        boolean success = paymentService.processPayment(userId, courseId);
        if (success) {
            courseService.enrollUser(userId,courseId);// enroll the user after the payment
            return ResponseEntity.ok("Payment successful, course purchased.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment failed.");
        }
    }

    @GetMapping("/stream/{courseId}/{videoId}")
    public ResponseEntity<String> streamVideo(@PathVariable Integer courseId,
                                              @PathVariable Integer videoId) {
        Integer userId = authUtil.getCurrentUserId();
        // Retrieve the course and video details
        Courses course = courseService.findById(courseId);
        Video video = videoService.findById(videoId);

        // Validate that the video belongs to the course
        if (video == null || !video.getCourses().getId().equals(course.getId())) {
            return ResponseEntity.badRequest().body("Invalid video or course.");
        }

        // Check if the video is free
        if (Boolean.TRUE.equals(video.getIsFree())) {
            String url = cloudinaryService.generateSignedUrl(video.getVideoUrl());
            return ResponseEntity.ok(url);
        }

        // For non-free videos, check if the user purchased the course
        boolean purchased = coursePurchaseService.hasUserPurchasedCourse(userId, course);
        if (!purchased) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied. Payment required to view this video.");
        }
         String url = cloudinaryService.generateSignedUrl(video.getVideoUrl());
        // If the user has purchased the course, return the video URL
        return ResponseEntity.ok(url);
    }

    //  Enroll a user in a course
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollUserInCourse(@PathVariable Integer courseId) {
        Integer userId = authUtil.getCurrentUserId();
        courseService.enrollUser(userId,courseId);
        return ResponseEntity.ok("User enrolled successfully in course ID: " + courseId);
    }
}
