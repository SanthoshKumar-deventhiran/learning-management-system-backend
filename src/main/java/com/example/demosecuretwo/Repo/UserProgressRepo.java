package com.example.demosecuretwo.Repo;


import com.example.demosecuretwo.Model.UserProgress;
import com.example.demosecuretwo.Model.UserProgressKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProgressRepo extends JpaRepository<UserProgress, UserProgressKey> {

    @Query("SELECT COUNT(up) FROM UserProgress up " +
            "WHERE up.user.id = :userId " +
            "AND up.video.courses.id = :courseId " +
            "AND up.completed = true")
    Integer countCompletedVideos(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}

