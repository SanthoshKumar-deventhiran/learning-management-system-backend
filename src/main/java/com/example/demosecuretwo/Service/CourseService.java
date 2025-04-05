package com.example.demosecuretwo.Service;

import com.example.demosecuretwo.DTO.CourseDTO;
import com.example.demosecuretwo.DTO.CourseStatsDTO;
import com.example.demosecuretwo.Exceptions.ResourceNotFoundException;
import com.example.demosecuretwo.Model.*;
import com.example.demosecuretwo.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private CourseEnrollmentRepo courseEnrollmentRepo;

    @Autowired
    private CoursePurchaseRepository coursePurchaseRepository;

    @Autowired
    private VideoService videoService;

    // Get all courses (Returns List<CourseDTO>)
    public List<CourseDTO> getAllCourses() {
        List<Courses> courses = courseRepo.findAll();
        return courses.stream().map(CourseDTO::new).collect(Collectors.toList());
    }

    public Courses findById(Integer courseId) {
        return courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // Get course by ID (Returns CourseDTO)
    public CourseDTO getCourseById(Integer id) {
        Courses course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return new CourseDTO(course);
    }

    // Enroll user in a course (Transaction Managed)
    @Transactional
    public void enrollUser(Integer userId, Integer courseId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Courses course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        // Create and save enrollment
        CourseEnrollment enrollment = new CourseEnrollment(course, user);
        courseEnrollmentRepo.save(enrollment);
    }

    //  Add a new course (Takes CourseDTO as Input, Returns CourseDTO)
    @Transactional
    public CourseDTO addCourse(CourseDTO courseDTO) throws IOException {
        Category category = categoryRepo.findByCategoryName(courseDTO.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + courseDTO.getCategoryName()));

        Courses course = new Courses();
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        course.setObjectives(courseDTO.getObjectives());
        course.setCategory(category);
        course.setLanguage(courseDTO.getLanguage());
        course.setLevel(courseDTO.getLevel());


        MultipartFile file = courseDTO.getImageFile();
        if (file != null && !file.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(file.getBytes());
            course.setImageUrl(uploadResult.get("secure_url")); // Store full image URL instead of public_id
        }
        Courses savedCourse = courseRepo.save(course);
        return new CourseDTO(savedCourse);
    }

    public CourseDTO editCourse(Integer id, CourseDTO courseDTO, MultipartFile imageFile,
                                MultipartFile[] videoFiles, List<Integer> deleteVideoIds) throws Exception {

        Courses course = courseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        // Update course details
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        course.setObjectives(courseDTO.getObjectives());
        course.setLanguage(courseDTO.getLanguage());
        course.setLevel(courseDTO.getLevel());

        // Replace image if new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            Map<String, String> uploadResult = cloudinaryService.uploadImage(imageFile.getBytes());
            course.setImageUrl(uploadResult.get("secure_url"));
        }

        // Delete videos if specified
        if (deleteVideoIds != null) {
            for (Integer videoId : deleteVideoIds) {
                videoService.deleteVideo(videoId);
            }
        }

        // Upload new videos if provide
        int i=0;
        if (videoFiles != null) {
            for (MultipartFile file : videoFiles) {
                videoService.uploadVideo(course.getId(), file, courseDTO.getVideos().get(i).getTitle(), courseDTO.getVideos().get(i).getIsFree());
                i++;
            }
        }

        Courses updatedCourse = courseRepo.save(course);
        return new CourseDTO(updatedCourse);
    }

    //  Remove a course by ID
    public void removeCourse(Integer courseId) {
        Courses course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
        courseRepo.delete(course);
    }

    public Integer getTotalStudents() {
        return courseEnrollmentRepo.countTotalStudents();
    }

    public Integer getStudentsEnrolledThisMonth() {
        return courseEnrollmentRepo.countStudentsEnrolledThisMonth();
    }

    public Double getTotalRevenue() {
        return coursePurchaseRepository.getTotalRevenue();
    }

    public Double getRevenueThisMonth() {
        return coursePurchaseRepository.getMonthlyRevenue();
    }

    public List<CourseStatsDTO> getCourseStatistics() {
        return courseRepo.getCourseStatistics();
    }
}
