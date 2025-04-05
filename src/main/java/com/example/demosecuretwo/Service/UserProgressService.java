package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Model.UserProgress;
import com.example.demosecuretwo.Model.UserProgressKey;
import com.example.demosecuretwo.Model.Users;
import com.example.demosecuretwo.Model.Video;
import com.example.demosecuretwo.Repo.UserProgressRepo;
import com.example.demosecuretwo.Repo.UserRepo;
import com.example.demosecuretwo.Repo.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProgressService {

    @Autowired
    private UserProgressRepo userProgressRepository;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private VideoRepo videoRepository;

    public void markVideoCompleted(Integer userId, Integer videoId) {
        // Fetch User and Video from DB before using them
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with ID: " + videoId));

        UserProgressKey key = new UserProgressKey(userId, videoId);
        Optional<UserProgress> existingProgress = userProgressRepository.findById(key);

        if (!existingProgress.isPresent()) {
            UserProgress newProgress = new UserProgress(user, video, true);
            userProgressRepository.save(newProgress);
        }

        //  Update Course Progress when a video is completed
        userCourseService.updateCourseProgress(userId, videoId);
    }
}
