package com.kiyata.ubg.admission.window;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/window")
public class WindowController {

    @Autowired
    private WindowService windowService;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createWindow(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody Window window) {

        String token = authorizationHeader.substring(7);
        Optional<Window> createdWindow = windowService.createWindow(token, window);

        if (createdWindow.isEmpty())
            return ResponseEntity.status(401).body("User not authorized");

        return ResponseEntity.ok(createdWindow.get());
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Window>> getActiveWindows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size)  {

        Pageable pageable = PageRequest.of(page, size);
        Page<Window> windows = windowService.getActiveWindows(pageable);
        return ResponseEntity.ok(windows);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> deleteWindow(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {

        String token = authorizationHeader.substring(7);
        if (!windowService.deleteWindow(token, id)) {
            return ResponseEntity.status(401).body("Unable to delete window");
        }

        return ResponseEntity.ok("Window deleted successfully");
    }
}
