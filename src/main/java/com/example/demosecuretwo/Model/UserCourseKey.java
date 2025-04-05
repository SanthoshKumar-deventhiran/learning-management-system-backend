package com.example.demosecuretwo.Model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserCourseKey implements Serializable {
    private Integer userId;
    private Integer courseId;

    public UserCourseKey() {}

    public UserCourseKey(Integer userId, Integer courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
