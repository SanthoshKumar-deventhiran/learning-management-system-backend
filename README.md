
# Learning Management System (LMS) - Backend

A Spring Boot–based backend for a Learning Management System (LMS) that provides RESTful APIs for managing courses, videos, and user authentication with JWT. It features role-based access control where **admins** can add, edit, and delete resources, while **users** can browse, purchase, and stream courses. The application integrates Razorpay for payment processing and uses Cloudinary for video storage/streaming. Data is stored in PostgreSQL, and the project is built with Java 21.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [API Endpoints](#api-endpoints)
- [Role-Based Access Control & Security](#role-based-access-control--security)
- [Future Improvements](#future-improvements)
- [License](#license)

---

## Features

- **User Authentication & Authorization:**
  - Signup and login with JWT-based authentication.
  - Passwords are encoded using BCrypt with strength 12.
  - Role-based access: 
    - **Admins:** Can add, edit, and delete courses and videos.
    - **Users:** Can browse courses, purchase, and stream videos.

- **Course & Video Management:**
  - Create, edit, and delete courses (admin-only).
  - Upload course images and multiple videos.
  - Secure video streaming via Cloudinary signed URLs.
  - Access control for free and paid content.

- **Payment Processing:**
  - Process payments using the Razorpay API.
  - Simulated payment confirmation for testing purposes.
  - Record course purchases upon successful payment.

- **Analytics & Error Handling:**
  - Track revenue and student enrollment statistics.
  - Centralized exception management for robust API responses.

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **Authentication:** JWT
- **Password Encoding:** BCrypt (strength 12)
- **Payment:** Razorpay API
- **File Storage/Streaming:** Cloudinary
- **Build Tool:** Maven

---

## Project Structure

```
LMProject
├── application.properties      # Configuration for DB, Cloudinary, Razorpay, JWT, etc.
├── DemoApplication.java        # Main Spring Boot Application class
├── config/                     # Configuration classes (e.g., security settings, CORS)
├── controller/                 # REST controllers for Users, Courses, Admin, and Videos
├── dto/                        # Data Transfer Objects for API requests/responses
├── model/                     # JPA entities (User, Course, Video, etc.)
├── exception/                  # Custom exceptions and global error handling
├── repository/                 # Spring Data JPA repositories
├── service/                    # Business logic and services (PaymentService, VideoService, etc.)
```

---

## Setup and Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/learning-management-system-backend.git
   cd learning-management-system-backend
   ```

2. **Configure the Database**
   - Create a PostgreSQL database (e.g., `lms_db`).
   - Update your database credentials in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/lms_db
     spring.datasource.username=your_db_username
     spring.datasource.password=your_db_password
     ```

3. **Configure Razorpay, Cloudinary, and JWT**
   - Add your Razorpay API key and secret:
     ```properties
     razorpay.api.key=YOUR_RAZORPAY_KEY
     razorpay.api.secret=YOUR_RAZORPAY_SECRET
     ```
   - Add your Cloudinary credentials:
     ```properties
     cloudinary.cloud_name=YOUR_CLOUD_NAME
     cloudinary.api_key=YOUR_CLOUDINARY_API_KEY
     cloudinary.api_secret=YOUR_CLOUDINARY_API_SECRET
     ```
   - Set your JWT properties:
     ```properties
     jwt.secret=YOUR_JWT_SECRET
     jwt.expiration=3600000
     ```

4. **Admin Account Setup**
   - **Admin Details:** Admin accounts are created manually in the database. Make sure to insert an admin record with the proper role to access admin endpoints.
   - **User Registration:** Users can sign up via the `/signup` endpoint. Their passwords will be securely hashed with BCrypt (strength 12).

5. **Build and Run**
   ```bash
   # For Linux/Mac:
   ./mvnw spring-boot:run

   # For Windows:
   mvnw spring-boot:run
   ```
   The application will be available at `http://localhost:8080`.

---

## API Endpoints

### Authentication
- **`POST /signup`**  
  Register a new user. Passwords are hashed using BCrypt.
- **`POST /login`**  
  Authenticate a user and receive a JWT.

### Courses
- **`GET /courses/all`**  
  Retrieve all available courses.
- **`GET /courses/{courseId}`**  
  Get details of a specific course.
- **`POST /courses/{courseId}/purchase`**  
  Purchase a course using Razorpay payment processing.
- **`GET /courses/stream/{courseId}/{videoId}`**  
  Stream a video with appropriate access verification.

### Admin (Admin-Only Access)
- **`POST /admin/course/add`**  
  Add a new course with videos.
- **`PUT /admin/{courseId}/edit`**  
  Edit an existing course.
- **`DELETE /admin/removeCourse/{courseId}`**  
  Delete a course.
- **`GET /admin/stats`**  
  Retrieve analytics such as revenue and enrollment statistics.

### Payments
- **PaymentService**  
  Handles Razorpay order creation and simulates payment confirmation.

---

## Role-Based Access Control & Security

- **Admins:**  
  - Admin records are manually maintained in the database.
  - Only admin users can access endpoints under `/admin/**` to manage courses and videos.
  
- **Users:**  
  - Users register through the signup endpoint and log in to receive a JWT.
  - JWT-based authentication secures user endpoints and restricts access as per role.

- **Security Configuration:**  
  - Security settings (such as CORS, JWT filters, etc.) are handled in the configuration classes inside the `config/` folder.

---

## Future Improvements

- **Frontend Integration:**  
  Develop a frontend using frameworks like React, Angular, or Vue for a complete LMS experience.

- **Enhanced Payment Confirmation:**  
  Integrate Razorpay webhooks for robust, real-time payment verification.

- **Additional Security Enhancements:**  
  Expand JWT capabilities (e.g., refresh tokens) and further refine access controls.

- **Monitoring & Logging:**  
  Implement advanced logging (using Logback) and application monitoring (with Spring Boot Actuator) for production environments.

---

## License

This project is licensed under the [MIT License](LICENSE).
```
