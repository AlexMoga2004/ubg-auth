package com.ubg.admission.user;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Document(collection = "user")
public class User {

    @Id
    private String id;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /* Default values not passed by users */
    private List<String> roles;

    private String profilePictureBase64Image;

    public void title() {
        firstname = toDisplayCase(firstname);
        lastname = toDisplayCase(lastname);
    }

    private String toDisplayCase(String s) {

        final String ACTIONABLE_DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf(c) >= 0);
        }
        return sb.toString();
    }
}