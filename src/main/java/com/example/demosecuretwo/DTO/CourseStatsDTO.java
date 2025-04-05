package com.example.demosecuretwo.DTO;

public class CourseStatsDTO {
    private Integer id;
    private String courseName;
    private Double coursePrice;
    private Long enrolledStudentCount;
    private Double totalRevenue;

    public CourseStatsDTO(Integer id,String courseName, Double coursePrice, Long enrolledStudentCount, Double totalRevenue) {
        this.id=id;
        this.courseName = courseName;
        this.coursePrice = coursePrice;
        this.enrolledStudentCount = enrolledStudentCount;
        this.totalRevenue = totalRevenue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Long getEnrolledStudentCount() {
        return enrolledStudentCount;
    }

    public void setEnrolledStudentCount(Long enrolledStudentCount) {
        this.enrolledStudentCount = enrolledStudentCount;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

