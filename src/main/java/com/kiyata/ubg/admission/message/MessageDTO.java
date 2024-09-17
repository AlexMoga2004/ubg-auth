package com.kiyata.ubg.admission.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class MessageDTO {

    @NotBlank(message = "Sender id is required")
    private String senderID;

    private String recipientID;
    private String role;
    private boolean isRole;

    @NotBlank(message = "Message subject is required")
    @Size(max = 45, message = "Message subject must be at most 45 characters")
    private String messageSubject;

    @NotBlank(message = "Message content is required")
    @Size(max = 2000, message = "Message content must be at most 2,000 characters")
    private String messageContent;

    public boolean getIsRole() {
        return isRole;
    }
}
