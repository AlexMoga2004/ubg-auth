package com.kiyata.ubg.admission.message;

import com.kiyata.ubg.admission.misc.JwtUtil;
import com.kiyata.ubg.admission.user.User;
import com.kiyata.ubg.admission.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/received")
    public ResponseEntity<Page<Message>> getReceivedMessages(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Message> messages = messageService.getMessagesByRecipientID(user.getId(), pageable);
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<Page<Message>> getSentMessages(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Pageable pageable = PageRequest.of(page, size);
            Page<Message> messages = messageService.getMessagesBySenderID(user.getId(), pageable);
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Message message) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);

        if (roles.contains("Admin")) {
            Message sentMessage = messageService.sendMessage(message);
            return ResponseEntity.ok(sentMessage);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Message> markMessageAsRead(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String id) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Message> messageOptional = messageService.markAsRead(id, user.getId());
            return messageOptional.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(404).body(null)); // Not Found
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }
}
