package com.example.demosecuretwo.Model;

import jakarta.persistence.*;

@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String videoUrl; // This will store the path of the uploaded video
    private Boolean isFree;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Courses courses;


    public Video() {}

    public Video(String title, String videoUrl, Boolean isFree, Courses courses) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.isFree = isFree;
        this.courses = courses;
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

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public Boolean getIsFree() { return isFree; }

    public void setIsFree(Boolean isFree) { this.isFree = isFree; }
}

