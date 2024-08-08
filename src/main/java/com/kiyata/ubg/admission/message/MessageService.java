package com.kiyata.ubg.admission.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Page<Message> getMessagesByRecipientID(String recipientID, Pageable pageable) {
        return messageRepository.findByRecipientID(recipientID, pageable);
    }

    public Page<Message> getMessagesBySenderID(String senderID, Pageable pageable) {
        return messageRepository.findBySenderID(senderID, pageable);
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Message> markAsRead(String messageId, String recipientID) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            if (message.getRecipientID().equals(recipientID)) {
                message.setRead(true);
                return Optional.of(messageRepository.save(message));
            }
        }
        return Optional.empty();
    }
}
