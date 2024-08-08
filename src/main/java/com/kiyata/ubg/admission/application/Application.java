package com.kiyata.ubg.admission.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Document(collection = "application")
public class Application {

    @Id
    private String id;

    @NotBlank(message = "Applicant ID is required")
    private String applicantID;

    @NotBlank(message = "Course ID is required")
    private String courseID;

    @NotBlank(message = "Submission date is required")
    private LocalDate submissionDate;

    /*
    TODO:
    The content of the application is likely to change based on the needs of the university,
    just leaving this as a String for now, but most likely will change in the private repo
     */
    @NotBlank(message = "Application content is required")
    @Size(min = 1_000, max=4_000, message = "Personal statement must be between 1,000 and 4,000 characters")
    private String content;

    /* Default values not passed by users */
    private String status; // TODO: enforce with ENUM, for now: "Pending" (default), "Accepted", "Rejected"

}
