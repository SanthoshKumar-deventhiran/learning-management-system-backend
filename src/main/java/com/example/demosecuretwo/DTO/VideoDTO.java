package com.example.demosecuretwo.DTO;

import com.example.demosecuretwo.Model.Video;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoDTO {
    private Integer id;
    private String title;
    private String videoUrl;
    private String courseName;

    @JsonProperty("isFree")  // Explicitly map "isFree" from JSON
    private Boolean isFree;

    public VideoDTO() {
    }

    public VideoDTO(String title, String videoUrl) {
        this.title = title;
        this.videoUrl = videoUrl;
    }

    public VideoDTO(Video video) {
        this.id = video.getId();
        this.title = video.getTitle();
        this.videoUrl = video.getVideoUrl();
        this.courseName = (video.getCourses() != null) ? video.getCourses().getName() : "NO COURSE";
        this.isFree = (video.getIsFree() != null) ? video.getIsFree() : false;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Boolean getIsFree() { return isFree; }
    public void setIsFree(Boolean isFree) { this.isFree = isFree; }
}
