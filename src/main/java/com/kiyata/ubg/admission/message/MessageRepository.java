package com.kiyata.ubg.admission.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    Page<Message> findByRecipientID(String recipientID, Pageable pageable);
    Page<Message> findBySenderID(String senderID, Pageable pageable);
}
