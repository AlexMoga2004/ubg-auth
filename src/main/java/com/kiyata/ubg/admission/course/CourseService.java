package com.kiyata.ubg.admission.course;

import com.kiyata.ubg.admission.misc.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<Course> createCourse(String token, Course course) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (!roles.contains("Admin"))
            return Optional.empty();

        Optional<Course> sameTitle = courseRepository.findByCourseTitle(course.getCourseTitle());
        if (sameTitle.isPresent())
            return Optional.empty();

        return Optional.of(courseRepository.save(course));
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public boolean deleteCourse(String token, String id) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles.contains("Admin")) {
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Course> updateCourse(String token, String id, Course course) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (roles.contains("Admin")) {
            course.setId(id);
            return Optional.of(courseRepository.save(course));
        }
        return Optional.empty();
    }
}
