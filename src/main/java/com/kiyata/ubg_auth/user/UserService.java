package com.kiyata.ubg_auth.user;

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

    public Optional<User> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
}
