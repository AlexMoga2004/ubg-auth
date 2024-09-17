package com.kiyata.ubg.admission.message;

import com.kiyata.ubg.admission.misc.JwtUtil;
import com.kiyata.ubg.admission.user.User;
import com.kiyata.ubg.admission.user.UserRepository;
import com.kiyata.ubg.admission.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/received")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Message>> getReceivedMessages(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "date")); // Sort by date descending
            Page<Message> messages = messageService.getMessagesByRecipientID(user.getId(), pageable);
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @GetMapping("/sent")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Page<Message>> getSentMessages(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.DESC, "date")); // Sort by date descending
            Page<Message> messages = messageService.getMessagesBySenderID(user.getId(), pageable);
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @PutMapping("/{id}/read")
    @CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/unread-count")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Map<String, Long>> getUnreadMessagesCount(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        String email = jwtUtil.extractUsername(token);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            long unreadCount = messageService.getUnreadMessagesCount(user.getId()); // Implement this method in MessageService
            return ResponseEntity.ok(Map.of("count", unreadCount)); // Return count as response
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Message> sendMessage(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody MessageDTO messageDTO) {
        String token = authorizationHeader.substring(7); // Remove "Bearer "
        List<String> roles = jwtUtil.extractRoles(token);

        if (roles.contains("Admin")) {
            // Create a list to hold messages for each recipient
            List<Message> messagesToSend = new ArrayList<>();

            // Check if the message is directed to a role
            if (messageDTO.getIsRole()) {
                List<User> usersWithRole = userService.findUsersByRole(messageDTO.getRole());
                for (User user : usersWithRole) {
                    Message message = convertToMessage(messageDTO, user.getId());
                    messagesToSend.add(message);
                }
            } else {
                // Create a message for a specific user
                Message message = convertToMessage(messageDTO, messageDTO.getRecipientID());
                messagesToSend.add(message);
            }

            // Send each message in the list
            for (Message message : messagesToSend) {
                messageService.sendMessage(message);
            }

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
    }

    // Helper method to convert MessageDTO to Message
    private Message convertToMessage(MessageDTO messageDTO, String recipientID) {
        return Message.builder()
                .senderID(messageDTO.getSenderID())
                .recipientID(recipientID)
                .date(LocalDateTime.now())
                .messageSubject(messageDTO.getMessageSubject())
                .messageContent(messageDTO.getMessageContent())
                .read(false) // Assuming the message is unread when sent
                .build();
    }
}
