package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.Model.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseEnrollmentRepo extends JpaRepository<CourseEnrollment, Integer> {
    // Count total students enrolled in all courses
    @Query("SELECT COUNT(DISTINCT e.user.id) FROM CourseEnrollment e")
    int countTotalStudents();

    // Count students enrolled in the current month across all courses
    @Query("SELECT COUNT(DISTINCT e.user.id) FROM CourseEnrollment e WHERE MONTH(e.enrollmentDate) = MONTH(CURRENT_DATE) AND YEAR(e.enrollmentDate) = YEAR(CURRENT_DATE)")
    int countStudentsEnrolledThisMonth();
}
