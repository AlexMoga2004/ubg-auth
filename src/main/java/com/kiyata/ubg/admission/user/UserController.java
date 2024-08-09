package com.kiyata.ubg.admission.user;

import com.kiyata.ubg.admission.user.UserService;
import com.kiyata.ubg.admission.user.UserUpdate;
import com.kiyata.ubg.admission.misc.JwtUtil;
import com.kiyata.ubg.admission.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users")
    public ResponseEntity<?> searchUsers(
            @RequestHeader("Authorization") String authorizationToken,
            @RequestParam(value = "searchTerm", required = false) String searchTerm) {

        List<User> users;
        String token = authorizationToken.substring(7); // Remove "bearer
        List<String> roles = jwtUtil.extractRoles(token);

        if (!roles.contains("Admin"))
            return ResponseEntity.status(401).body("Unauthorized request");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            users = userService.searchUsersByName(searchTerm.trim());
        } else {
            users = userService.getAllUsers();
        }

        return ResponseEntity.ok(users);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/users/{id}")
    public ResponseEntity<?> findUserById(@PathVariable String id) {
        Optional<User> user = userService.findById(id); // Assume you have a method in userService
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.status(404).body("User not found");
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/check-token")
    public ResponseEntity<?> checkTokenExpiration(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "

        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body("Token is expired");
        }

        return ResponseEntity.ok("Token is valid");
    }
}
