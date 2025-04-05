package com.example.demosecuretwo.Repo;

import com.example.demosecuretwo.Model.CoursePurchase;
import com.example.demosecuretwo.Model.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoursePurchaseRepository extends JpaRepository<CoursePurchase, Long> {
    boolean existsByUserIdAndCourse(Integer userId, Courses course);
    // Total revenue from all purchases
    @Query("SELECT COALESCE(SUM(cp.amount), 0) FROM CoursePurchase cp")
    double getTotalRevenue();

    // Monthly revenue (current month)
     @Query("SELECT COALESCE(SUM(cp.amount), 0) FROM CoursePurchase cp WHERE MONTH(cp.purchaseDate) = MONTH(CURRENT_DATE) AND YEAR(cp.purchaseDate) = YEAR(CURRENT_DATE)")
     double getMonthlyRevenue();
}

