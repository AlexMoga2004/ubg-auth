package com.kiyata.ubg.admission.course;

import com.kiyata.ubg.admission.course.Course;
import com.kiyata.ubg.admission.course.CourseService;
import com.kiyata.ubg.admission.misc.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody Course course) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        Optional<Course> createdCourse = courseService.createCourse(token, course);

        if (createdCourse.isPresent())
            return ResponseEntity.ok(createdCourse.get());

        return ResponseEntity.status(401).body("Unauthorized request");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        boolean deleted = courseService.deleteCourse(token, id);

        if (deleted)
            return ResponseEntity.ok("Course deleted");

        return ResponseEntity.status(401).body("Unauthorized request");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id,
            @Valid @RequestBody Course course) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        Optional<Course> updatedCourse = courseService.updateCourse(token, id, course);

        if (updatedCourse.isPresent())
            return ResponseEntity.ok(updatedCourse.get());
        return ResponseEntity.status(401).body("unauthorised request");
    }
}
