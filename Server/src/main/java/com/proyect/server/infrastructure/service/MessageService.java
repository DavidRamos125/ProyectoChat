package com.proyect.server.infrastructure.service;

import com.proyect.server.infrastructure.entity.EMessage;
import com.proyect.server.infrastructure.repository.MessageRepository;
import com.proyect.server.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<EMessage> listAll() {
        return messageRepository.findAll();
    }

    public List<EMessage> listByType(EMessage.ContentType type) {
        return messageRepository.findByContentType(type);
    }

    public List<EMessage> listMessagesSent(Integer userId) {
        return messageRepository.findMessagesSentByUser(userId);
    }

    public List<EMessage> listMessagesReceived(Integer userId) {
        return messageRepository.findMessagesReceivedByUser(userId);
    }

    public Optional<EMessage> findById(Integer id) {
        return messageRepository.findById(id);
    }

    public EMessage saveMessage(EMessage message) {
        return messageRepository.save(message);
    }
}
