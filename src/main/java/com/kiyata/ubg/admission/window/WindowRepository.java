package com.kiyata.ubg.admission.window;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WindowRepository extends MongoRepository<Window, String> {
    List<Window> findAllByEndAfterOrderByStartAsc(LocalDateTime dateTime);
    Page<Window> findByEndAfterOrderByStartAsc(LocalDateTime now, Pageable pageable);
}
