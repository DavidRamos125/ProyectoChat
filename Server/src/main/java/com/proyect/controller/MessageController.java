package com.proyect.controller;

import com.proyect.application.Logger;
import com.proyect.domain.*;
import com.proyect.DTO.MessageDTO;
import com.proyect.DTO.mapper.*;
import com.proyect.persistence.DAO.MessageDAO;
import com.proyect.persistence.DAO.SessionDAO;
import com.proyect.persistence.DAO.UserDAO;
import com.proyect.factory.InternalFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MessageController {

    private MessageDAO messageDAO;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;
    private Logger logger;
    private final String className = this.getClass().getSimpleName();

    public MessageController() {
        this.messageDAO = InternalFactory.getMessageDAO();
        this.userDAO = InternalFactory.getUserDAO();
        this.sessionDAO = InternalFactory.getSessionDAO();
        this.logger = Logger.getInstance();
    }
    public MessageDTO insertMessage(MessageDTO messageDTO) {
        try {
            // Convertir DTO a objeto de dominio
            Message message = MessageMapper.dtoToMessage(messageDTO);

            // Insertar usando el DAO
            messageDAO.insert(message);

            // Log de la acción
            String contentPreview = "TEXT".equals(messageDTO.getType()) ?
                    (messageDTO.getTextContent().length() > 30 ?
                            messageDTO.getTextContent().substring(0, 30) + "..." : messageDTO.getTextContent()) :
                    "FILE: " + messageDTO.getFileName();

            logger.logAccion("Mensaje enviado de " + messageDTO.getSender().getUsername() +
                    " a " + messageDTO.getReceiver().getUsername() + ": " + contentPreview, className);

            // Retornar el DTO actualizado (con ID generado)
            return MessageMapper.messageToDTO(message);

        } catch (SQLException e) {
            logger.logAccion("Error al insertar mensaje: " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Obtener mensajes por usuario y devolver como lista de DTOs
     */
    public List<MessageDTO> getMessagesByUser(int userId) {
        try {
            List<Message> messages = messageDAO.getMessagesByUser(userId);
            logger.logAccion("Obtenidos " + messages.size() + " mensajes para usuario ID: " + userId, className);

            // Convertir lista de Message a lista de MessageDTO
            return messages.stream()
                    .map(MessageMapper::messageToDTO)
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            logger.logAccion("Error al obtener mensajes del usuario " + userId + ": " + e.getMessage(), className);
            return List.of();
        }
    }

    /**
     * Obtener un mensaje específico por su ID como DTO
     */
    public MessageDTO getMessageById(int messageId) {
        try {
            Message message = messageDAO.findById(messageId);
            if (message != null) {
                return MessageMapper.messageToDTO(message);
            } else {
                logger.logAccion("Mensaje no encontrado: " + messageId, className);
                return null;
            }
        } catch (SQLException e) {
            logger.logAccion("Error al obtener mensaje por ID: " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Método de conveniencia para enviar mensaje de texto
     */
    public MessageDTO sendTextMessage(int senderId, int receiverId, String sessionSenderId, String text) {
        try {
            // Obtener objetos de dominio
            User sender = userDAO.findById(senderId).orElse(null);
            User receiver = userDAO.findById(receiverId).orElse(null);
            Session sessionSender = sessionDAO.findById(sessionSenderId).orElse(null);

            if (sender == null || receiver == null || sessionSender == null) {
                logger.logAccion("Error: Usuario o sesión no encontrados al enviar mensaje", className);
                return null;
            }

            // Crear MessageDTO
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSender(UserMapper.userToDTO(sender));
            messageDTO.setReceiver(UserMapper.userToDTO(receiver));
            messageDTO.setSessionSender(SessionMapper.sessionToDTO(sessionSender));
            messageDTO.setType("TEXT");
            messageDTO.setTextContent(text);
            messageDTO.setDate(Timestamp.valueOf(LocalDateTime.now()));

            // Usar el método principal de inserción
            return insertMessage(messageDTO);

        } catch (SQLException e) {
            logger.logAccion("Error al enviar mensaje de texto: " + e.getMessage(), className);
            return null;
        }
    }
}
