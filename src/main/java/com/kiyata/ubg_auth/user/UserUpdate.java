package com.kiyata.ubg_auth.user;

import lombok.Data;

@Data
public class UserUpdate {
    private String currentEmail;
    private String currentPassword;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
