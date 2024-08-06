package com.ubg.admission.user;

import lombok.Data;

@Data
public class UserUpdate {
    private String originalEmail;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String profilePictureBase64Image;
}
