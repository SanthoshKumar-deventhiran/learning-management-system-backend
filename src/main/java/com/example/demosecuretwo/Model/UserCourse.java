package com.example.demosecuretwo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_courses")
public class UserCourse {

    @EmbeddedId //composite primary key with clear encapsulation.
    private UserCourseKey id;// without it the database doesn't take it as the user and course as the composite key

    @ManyToOne
    @MapsId("userId") //@MapsId to map fields in the embedded key to foreign key relationships.
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Courses course;

    private double progress = 0.0;

    // Constructors
    public UserCourse() {}

    public UserCourse(Users user, Courses course, double progress) {
        this.id = new UserCourseKey(user.getId(), course.getId());
        this.user = user;
        this.course = course;
        this.progress = progress;
    }

    public UserCourseKey getId() {
        return id;
    }

    public void setId(UserCourseKey id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
