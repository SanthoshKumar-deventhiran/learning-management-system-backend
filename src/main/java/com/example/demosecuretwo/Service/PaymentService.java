package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Model.Courses;
import com.example.demosecuretwo.Repo.CourseRepo;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private CoursePurchaseService coursePurchaseService;

    @Autowired
    private CourseRepo courseRepo;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    public boolean processPayment(Integer userId, Integer courseId) {
        try {
            // Retrieve the course details
            Courses course = courseRepo.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

            // Initialize the Razorpay client
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);

            // Create an order request
            JSONObject orderRequest = new JSONObject();
            double amountInPaise = course.getPrice() * 100;
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", UUID.randomUUID().toString());

            Order order = razorpayClient.orders.create(orderRequest);
           // logger.info("Razorpay order created: {}", order.get("id"));

            // Payment confirmation should be handled via webhooks
            boolean paymentSuccessful = simulatePaymentConfirmation(order);

            if (paymentSuccessful) {
                coursePurchaseService.recordPurchase(userId, course, course.getPrice());
                logger.info("Payment successful for user {} and course {}", userId, courseId);
                return true;
            }

            logger.warn("Payment failed for user {} and course {}", userId, courseId);
            return false;
        } catch (RazorpayException e) {
            logger.error("Error creating Razorpay order: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            return false;
        }
    }

    private boolean simulatePaymentConfirmation(Order order) {
        // Placeholder logic for testing
        return true;
    }
}
