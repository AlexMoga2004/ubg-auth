package com.kiyata.ubg.admission.application;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Course ID is required")
    private String courseID;

    private LocalDate submissionDate;

    /* Default values not passed by users */
    private String status; // TODO: enforce with ENUM, for now: "Pending", "Accepted", "Rejected"

}
