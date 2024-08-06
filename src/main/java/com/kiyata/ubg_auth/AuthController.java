package com.kiyata.ubg_auth;

import com.kiyata.ubg_auth.misc.JwtUtil;
import com.kiyata.ubg_auth.user.User;
import com.kiyata.ubg_auth.user.UserService;
import com.kiyata.ubg_auth.user.UserUpdate;
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

    @Autowired
    JwtUtil jwtUtil;

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

        if (authenticatedUser.isPresent()) {
            String token = jwtUtil.generateToken(email);
            Map<String, Object> response = new HashMap<>();

            response.put("token", token);
            response.put("user", authenticatedUser.get());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdate request) {
        // Pass the user object along with the current password for verification
        Optional<User> updatedUserResponse = userService.updateUser(request);
        if (updatedUserResponse.isPresent()) {
            return ResponseEntity.ok("User updated successfully");
        }

        Map<String, String> response = new HashMap<>();
        response.put("password", "Current email / password is incorrect");
        return ResponseEntity.status(401).body(response);
    }

}
