package com.kiyata.ubg.admission.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@Document(collection = "course")
public class Course {

    @Id
    private String id;

    @NotBlank(message = "Course title is required")
    private String courseTitle;

    @Size(max = 2_000, message = "Course description must not exceed 2000 characters")
    private String courseDescription;

    @NotBlank(message = "Course must have image")
    private String courseBase64Image;

}
