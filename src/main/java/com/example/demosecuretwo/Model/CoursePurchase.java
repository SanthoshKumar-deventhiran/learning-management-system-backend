package com.example.demosecuretwo.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CoursePurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // You might have a relationship to a User entity here. For simplicity, we store the user id.
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;

    private LocalDateTime purchaseDate;

    private Double amount; // Amount paid

    // Constructors, getters, and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

