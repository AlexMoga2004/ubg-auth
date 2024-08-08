package com.kiyata.ubg.admission.application;

import com.kiyata.ubg.admission.misc.JwtUtil;
import com.kiyata.ubg.admission.user.User;
import com.kiyata.ubg.admission.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Application>> getAllApplications(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);
        String email = jwtUtil.extractUsername(token);

        if (roles.contains("Admin")) {
            List<Application> applications = applicationService.getAllApplications();
            return ResponseEntity.ok(applications);
        } else {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<Application> applications = applicationService.getApplicationsByApplicantId(user.getId());
                return ResponseEntity.ok(applications);
            } else {
                return ResponseEntity.status(403).body(null); // Forbidden
            }
        }
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Application> getApplicationById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);
        String email = jwtUtil.extractUsername(token);

        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isPresent()) {
            Application app = application.get();
            if (roles.contains("Admin")) {
                return ResponseEntity.ok(app);
            } else {
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent() && userOptional.get().getId().equals(app.getApplicantID())) {
                    return ResponseEntity.ok(app);
                } else {
                    return ResponseEntity.status(403).body(null); // Forbidden
                }
            }
        } else {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Application> createApplication(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Application application) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && userOptional.get().getId().equals(application.getApplicantID())) {
            Application createdApplication = applicationService.createApplication(application);
            return ResponseEntity.ok(createdApplication);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Application> updateApplication(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id,
            @RequestBody Application updatedApplication) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);
        String email = jwtUtil.extractUsername(token);

        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isPresent()) {
            Application app = application.get();
            if (roles.contains("Admin")) {
                Optional<Application> updatedApp = applicationService.updateApplication(id, updatedApplication);
                return updatedApp.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.status(404).body(null)); // Not Found
            } else {
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent() && userOptional.get().getId().equals(app.getApplicantID())) {
                    Optional<Application> updatedApp = applicationService.updateApplication(id, updatedApplication);
                    return updatedApp.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.status(404).body(null)); // Not Found
                } else {
                    return ResponseEntity.status(403).body(null); // Forbidden
                }
            }
        } else {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Void> deleteApplication(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);
        String email = jwtUtil.extractUsername(token);

        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isPresent()) {
            Application app = application.get();
            if (roles.contains("Admin") || app.getApplicantID().equals(email)) {
                applicationService.deleteApplication(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(403).build(); // Forbidden
            }
        } else {
            return ResponseEntity.status(404).build(); // Not Found
        }
    }
}
