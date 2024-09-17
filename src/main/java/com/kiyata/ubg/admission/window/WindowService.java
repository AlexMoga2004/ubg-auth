package com.kiyata.ubg.admission.window;

import com.kiyata.ubg.admission.misc.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WindowService {

    @Autowired
    private WindowRepository windowRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Optional<Window> createWindow(String token, Window window) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (!roles.contains("Admin"))
            return Optional.empty();

        return Optional.of(windowRepository.save(window));
    }

    public Page<Window> getActiveWindows(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return windowRepository.findByEndAfterOrderByStartAsc(now, pageable);
    }

    public boolean deleteWindow(String token, String id) {
        List<String> roles = jwtUtil.extractRoles(token);
        if (!roles.contains("Admin"))
            return false;

        Optional<Window> window = windowRepository.findById(id);

        if (window.isPresent()) {
            windowRepository.delete(window.get());
            return true;
        }

        return false;
    }
}
