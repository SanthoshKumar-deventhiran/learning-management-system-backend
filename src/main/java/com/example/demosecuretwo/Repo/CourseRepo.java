package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.DTO.CourseStatsDTO;
import com.example.demosecuretwo.Model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Courses, Integer> {

    @Query("SELECT new com.example.demosecuretwo.DTO.CourseStatsDTO(c.id,c.name, c.price, \n" +
            "       (SELECT COUNT(e) FROM CourseEnrollment e WHERE e.course = c), \n" +
            "       (SELECT COALESCE(SUM(cp.amount), 0) FROM CoursePurchase cp WHERE cp.course = c)) \n" +
            "FROM Courses c\n")
    List<CourseStatsDTO> getCourseStatistics();
}
