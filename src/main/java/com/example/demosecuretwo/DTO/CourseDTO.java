package com.example.demosecuretwo.DTO;

import com.example.demosecuretwo.Model.Courses;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String categoryName;
    private String imageUrl; // Store image URL for retrieval
    @JsonIgnore
    private MultipartFile imageFile;
    private List<String> objectives;
    private String language;
    private String level;
    private List<VideoDTO> videos; // List of video details

    public CourseDTO() { }

    public CourseDTO(Courses course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.price = course.getPrice();
        this.categoryName = course.getCategory() != null ? course.getCategory().getCategoryName() : null;
        this.imageUrl = course.getImageUrl();
        this.language=course.getLanguage();
        this.objectives=course.getObjectives();
        this.level= course.getLevel();
        if (course.getVideos() != null) {
            // Traditional way to convert List<Video> to List<VideoDTO>
//            List<VideoDTO> videoDTOs = new ArrayList<>();
//            for (Video video : videos) {
//                videoDTOs.add(new VideoDTO(video));
//            }
            this.videos = course.getVideos().stream()
                    .map(VideoDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.videos = null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public List<VideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTO> videos) {
        this.videos = videos;
    }

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
