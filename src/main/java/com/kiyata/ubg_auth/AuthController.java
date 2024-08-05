package com.kiyata.ubg_auth;

import com.kiyata.ubg_auth.user.User;
import com.kiyata.ubg_auth.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        Optional<User> registeredUser = userService.registerUser(user);
        if (registeredUser.isPresent()) return ResponseEntity.ok("User registered successfully");
        return ResponseEntity.status(401).body("User already registered with this email");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Optional<User> authenticatedUser = userService.authenticate(email, password);

        if (authenticatedUser.isPresent())
            return ResponseEntity.ok(authenticatedUser.get());

        return ResponseEntity.status(401).body("Invalid email or password");
    }
}
