package com.kiyata.ubg_auth;

import com.kiyata.ubg_auth.user.User;
import com.kiyata.ubg_auth.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        Optional<User> registeredUser = userService.registerUser(user);
        if (registeredUser.isPresent()) return ResponseEntity.ok("User registered successfully");

        Map<String, String> response = new HashMap<>();
        response.put("email", "User already registered with this email");
        return ResponseEntity.status(401).body(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
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
