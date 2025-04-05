package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Model.CoursePurchase;
import com.example.demosecuretwo.Model.Courses;
import com.example.demosecuretwo.Repo.CoursePurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CoursePurchaseService {
    @Autowired
    private CoursePurchaseRepository coursePurchaseRepository;

    public boolean hasUserPurchasedCourse(Integer userId, Courses course) {
        return coursePurchaseRepository.existsByUserIdAndCourse(userId, course);
    }

    public void recordPurchase(Integer userId, Courses course, Double amount) {
        CoursePurchase purchase = new CoursePurchase();
        purchase.setUserId(userId);
        purchase.setCourse(course);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setAmount(amount);
        coursePurchaseRepository.save(purchase);
    }
}

