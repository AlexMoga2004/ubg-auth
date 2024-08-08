package com.kiyata.ubg.admission.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@Document(collection = "message")
public class Message {

    @Id
    private String id;

    @NotBlank(message = "Sender id is required")
    private String senderID;

    @NotBlank(message = "Recipient id is required")
    private String recipientID;

    @NotBlank(message = "Message send date is required")
    private LocalDate date;

    @Size(max = 2_000, message = "Message content is required")
    private String messageContent;

    private boolean read;
}
