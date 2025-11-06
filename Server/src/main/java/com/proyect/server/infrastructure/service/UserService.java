package com.proyect.server.infrastructure.service;

import com.proyect.server.infrastructure.entity.EUser;
import com.proyect.server.infrastructure.repository.UserRepository;
import com.proyect.server.infrastructure.repository.SessionRepository;
import com.proyect.server.infrastructure.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final MessageRepository messageRepository;

    public UserService(UserRepository userRepository,
                       SessionRepository sessionRepository,
                       MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
    }

    public EUser createUser(String username) {
        EUser user = new EUser();
        user.setUsername(username);
        user.setAccepted(false);
        return userRepository.save(user);
    }

    // Aceptar usuario (servidor)
    public Optional<EUser> acceptUser(Integer userId) {
        return userRepository.findById(userId).map(user -> {
            user.setAccepted(true);
            return userRepository.save(user);
        });
    }

    // Listar todos los usuarios
    public List<EUser> listAllUsers() {
        return userRepository.findAll();
    }

    // Usuarios aceptados
    public List<EUser> listAcceptedUsers() {
        return userRepository.findByAccepted(true);
    }

    // Usuarios conectados
    public List<EUser> listConnectedUsers() {
        return userRepository.findConnectedUsers();
    }

    // Usuarios desconectados
    public List<EUser> listDisconnectedUsers() {
        return userRepository.findDisconnectedUsers();
    }

    // Info completa del usuario (para API REST)
    public Optional<UserInfoDTO> getUserInfo(Integer userId) {
        return userRepository.findById(userId).map(user -> {
            Long sent = userRepository.countMessagesSentByUser(userId);
            Long received = userRepository.countMessagesReceivedByUser(userId);
            Long activeSessions = sessionRepository.countActiveSessionsByUser(userId);
            String state = activeSessions > 0 ? "CONECTADO" : "DESCONECTADO";

            return new UserInfoDTO(
                    user.getId(),
                    user.getUsername(),
                    sent,
                    received,
                    state
            );
        });
    }

    // DTO interno
    public static record UserInfoDTO(
            Integer id,
            String username,
            Long sentMessages,
            Long receivedMessages,
            String state
    ) {}
}
