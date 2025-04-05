package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Model.*;
import com.example.demosecuretwo.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCourseService {

    @Autowired
    private UserProgressRepo userProgressRepository;

    @Autowired
    private VideoRepo videoRepository;

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private UserCourseRepo userCourseRepository;

    @Autowired
    private UserRepo userRepository;  // Add UserRepo to fetch Users

    public void updateCourseProgress(Integer userId, Integer videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with ID: " + videoId));

        Integer courseId = video.getCourses().getId(); // Corrected path

        Integer totalVideos = videoRepository.countByCourseId(courseId);
        Integer completedVideos = userProgressRepository.countCompletedVideos(userId, courseId);

        double progress = (completedVideos * 100.0) / totalVideos;

        // Fetch Users and Courses from the database
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Courses course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        //  Use fetched entities
        UserCourseKey key = new UserCourseKey(userId, courseId);
        UserCourse userCourse = userCourseRepository.findById(key)
                .orElse(new UserCourse(user, course, 0.0));

        userCourse.setProgress(progress);
        userCourseRepository.save(userCourse);
    }

    public double getCourseProgress(Integer userId, Integer courseId) {
        return userCourseRepository.findById(new UserCourseKey(userId, courseId))
                .map(UserCourse::getProgress)
                .orElse(0.0);
    }
}
