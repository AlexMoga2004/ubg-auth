package com.kiyata.ubg_auth.user;

import com.kiyata.ubg_auth.misc.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String title(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
    }

    public Optional<User> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("Student");
        user.setProfilePicture(ImageUtil.getDefaultProfilePicture());

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

    public Optional<User> updateUser(UserUpdate request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getCurrentEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
                return Optional.empty();

            if (request.getFirstname() != null) user.setFirstname(request.getFirstname());
            if (request.getLastname() != null) user.setLastname(request.getLastname());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));

            return Optional.of(userRepository.save(user));
        }

        return Optional.empty();
    }
}
