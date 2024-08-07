package com.kiyata.ubg.admission.course;

import com.kiyata.ubg.admission.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCourseTitle(String courseTitle);
}
