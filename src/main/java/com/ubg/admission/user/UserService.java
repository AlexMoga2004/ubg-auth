package com.ubg.admission.user;

import com.ubg.admission.misc.ImageUtil;
import com.ubg.admission.misc.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    JwtUtil jwtUtil;

    private String title(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
    }

    public Optional<User> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of("Student"));
        user.setProfilePictureBase64Image(ImageUtil.getDefaultProfilePicture());

        // Capitalise entries correctly
        user.title();

        Optional<User> sameEmail = userRepository.findByEmail(user.getEmail());
        if (sameEmail.isPresent()) return Optional.empty();
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword()))
                return Optional.of(user);
        }

        return Optional.empty();
    }

    public Optional<User> updateUser(String token, UserUpdate request) {
        String email = jwtUtil.extractUsername(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        List<String> roles = jwtUtil.extractRoles(token);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            boolean isAdmin = roles != null && roles.contains("Admin");
            boolean isSameUser = email.equals(request.getOriginalEmail());

            if (isAdmin || isSameUser) {
                if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
                if (request.getLastname() != null) user.setLastname(request.getLastname());
                if (request.getEmail() != null) user.setEmail(request.getEmail());
                if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
                if (request.getProfilePictureBase64Image() != null) user.setProfilePictureBase64Image(request.getProfilePictureBase64Image());

                return Optional.of(userRepository.save(user));
            }
        }

        return Optional.empty();
    }
}
