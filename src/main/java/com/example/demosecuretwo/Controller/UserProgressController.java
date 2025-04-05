package com.example.demosecuretwo.Controller;

import com.example.demosecuretwo.Config.AuthUtil;
import com.example.demosecuretwo.Service.UserCourseService;
import com.example.demosecuretwo.Service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
public class UserProgressController {

    @Autowired
    private UserProgressService userProgressService;

    @Autowired
    private UserCourseService userCourseService;

    @Autowired
    private AuthUtil authUtil;
 // front end code ;

//    document.addEventListener("DOMContentLoaded", function () {
//    const video = document.getElementById("videoPlayer");
//        let reported = false;
//
//        video.addEventListener("timeupdate", () => {
//                let progress = (video.currentTime / video.duration) * 100;
//
//        if (progress > 90 && !reported) { //  If video reaches 90%
//            reported = true;
//
//            fetch(`/progress/complete-video/${video.dataset.videoId}`, {
//                method: "POST",
//                        headers: { "Content-Type": "application/json" }
//            })
//            .then(response => response.text())
//                    .then(data => console.log(data))
//                    .catch(error => console.error("Error:", error));
//        }
//    });
//    });

    // Mark a Video as Completed
    @PostMapping("/complete-video/{videoId}")
    public ResponseEntity<String> completeVideo(
            @PathVariable Integer videoId) {
        Integer userId = authUtil.getCurrentUserId();
        userProgressService.markVideoCompleted(userId, videoId);
        return ResponseEntity.ok("Video marked as completed!");
    }

    // Get Course Progress for a User
    @GetMapping("/course-progress/{courseId}")
    public ResponseEntity<Double> getCourseProgress(
            @PathVariable Integer courseId) {
        Integer userId = authUtil.getCurrentUserId();
        double progress = userCourseService.getCourseProgress(userId, courseId);
        return ResponseEntity.ok(progress);
    }
}

