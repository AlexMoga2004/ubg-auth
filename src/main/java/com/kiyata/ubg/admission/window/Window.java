package com.kiyata.ubg.admission.window;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Document(collection = "window")
public class Window {

    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Start time is required")
    private LocalDateTime start;

    @NotNull(message = "End time is required")
    private LocalDateTime end;

    private String note;

    private boolean hasEnded() {
        return end.isBefore(LocalDateTime.now());
    }
}
