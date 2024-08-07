package com.kiyata.ubg.admission;

import com.kiyata.ubg.admission.user.UserService;
import com.kiyata.ubg.admission.user.UserUpdate;
import com.kiyata.ubg.admission.misc.JwtUtil;
import com.kiyata.ubg.admission.user.User;
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
            User user = authenticatedUser.get();
            String token = jwtUtil.generateToken(email, user.getRoles());
            Map<String, Object> response = new HashMap<>();

            response.put("token", token);
            response.put("user", authenticatedUser.get());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserUpdate request) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        Optional<User> updatedUserResponse = userService.updateUser(token, request);
        if (updatedUserResponse.isPresent()) {
            return ResponseEntity.ok("User updated successfully");
        }

        Map<String, String> response = new HashMap<>();
        response.put("password", "Current email / password is incorrect");
        return ResponseEntity.status(401).body(response);
    }

}
