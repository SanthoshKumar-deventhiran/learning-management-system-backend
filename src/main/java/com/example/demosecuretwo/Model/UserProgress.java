package com.example.demosecuretwo.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_progress")
public class UserProgress {

    @EmbeddedId
    private UserProgressKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @MapsId("videoId")
    @JoinColumn(name = "video_id")
    private Video video;

    private boolean completed;

    // Constructors
    public UserProgress() {}

    public UserProgress(Users user, Video video, boolean completed) {
        this.id = new UserProgressKey(user.getId(), video.getId());
        this.user = user;
        this.video = video;
        this.completed = completed;
    }

    public UserProgressKey getId() {
        return id;
    }

    public void setId(UserProgressKey id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
