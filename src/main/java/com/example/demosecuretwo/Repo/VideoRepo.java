package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepo extends JpaRepository<Video, Integer> {
    // Find videos by course id since lessons have been removed
    //List<Video> findByCourseId(Integer courseId);

    // Count total videos in a specific course
    @Query("SELECT COUNT(v) FROM Video v WHERE v.courses.id = :courseId")
    Integer countByCourseId(@Param("courseId") Integer courseId);

}

