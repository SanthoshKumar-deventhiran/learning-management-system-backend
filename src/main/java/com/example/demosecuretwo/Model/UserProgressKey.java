package com.example.demosecuretwo.Model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserProgressKey implements Serializable {
    private Integer userId;
    private Integer videoId;

    // Constructors
    public UserProgressKey() {}

    public UserProgressKey(Integer userId, Integer videoId) {
        this.userId = userId;
        this.videoId = videoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
}

