package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.Model.UserCourse;
import com.example.demosecuretwo.Model.UserCourseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepo extends JpaRepository<UserCourse, UserCourseKey> {
}
