package com.proyect.controller;

import com.proyect.application.Logger;
import com.proyect.domain.User;
import com.proyect.DTO.UserDTO;
import com.proyect.DTO.mapper.UserMapper;
import com.proyect.persistence.DAO.UserDAO;
import com.proyect.factory.InternalFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserController {

    private UserDAO userDAO;
    private Logger logger;
    private final String className = this.getClass().getSimpleName();

    public UserController() {
        this.userDAO = InternalFactory.getUserDAO();
        this.logger = Logger.getInstance();
    }

    public UserDTO createUser(UserDTO userDTO) {
        try {
            User user = UserMapper.dtoToUser(userDTO);
            user.setAccepted(false);
            // Usar el DAO para insertar
            userDAO.insert(user);

            // Log de la acción
            logger.logAccion("Usuario creado: " + user.getUsername(), className);

            return UserMapper.userToDTO(user);

        } catch (SQLException e) {
            logger.logAccion("Error al crear usuario: " + userDTO.getUsername() + " - " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Aprobar un usuario por su ID
     */
    public UserDTO approveUser(int userId) {
        try {
            // Verificar que el usuario existe
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) {
                logger.logAccion("Intento de aprobar usuario no existente: " + userId, className);
                return null;
            }

            // Actualizar en la base de datos
            userDAO.updateStatus(userId, true);

            // Obtener el usuario actualizado
            User user = userOpt.get();
            user.setAccepted(true); // Actualizar el objeto local

            // Log de la acción
            logger.logAccion("Usuario aprobado: " + user.getUsername() + " (ID: " + userId + ")", className);

            // Convertir a DTO y retornar
            return UserMapper.userToDTO(user);

        } catch (SQLException e) {
            logger.logAccion("Error al aprobar usuario ID: " + userId + " - " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Obtener un usuario por su ID y devolver como DTO
     */
    public UserDTO getUserById(int userId) {
        try {
            Optional<User> user = userDAO.findById(userId);
            if (user.isPresent()) {
                return UserMapper.userToDTO(user.get());
            } else {
                logger.logAccion("Usuario no encontrado: " + userId, className);
                return null;
            }
        } catch (SQLException e) {
            logger.logAccion("Error al obtener usuario: " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Rechazar un usuario (marcar como no aceptado)
     */
    public UserDTO rejectUser(int userId) {
        try {
            // Verificar que el usuario existe
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) {
                logger.logAccion("Intento de rechazar usuario no existente: " + userId, className);
                return null;
            }

            // Actualizar en la base de datos
            userDAO.updateStatus(userId, false);

            // Obtener el usuario actualizado
            User user = userOpt.get();
            user.setAccepted(false);

            logger.logAccion("Usuario rechazado: " + user.getUsername() + " (ID: " + userId + ")", className);
            return UserMapper.userToDTO(user);

        } catch (SQLException e) {
            logger.logAccion("Error al rechazar usuario: " + e.getMessage(), className);
            return null;
        }
    }

    /**
     * Obtener lista de usuarios no aceptados como DTOs
     */
    public List<UserDTO> getNonAcceptedUsers() {
        try {
            List<User> users = userDAO.findNonAcceptedUsers();
            logger.logAccion("Obtenidos " + users.size() + " usuarios no aceptados", className);

            // Convertir lista de User a lista de UserDTO
            return users.stream()
                    .map(UserMapper::userToDTO)
                    .collect(Collectors.toList());

        } catch (SQLException e) {
            logger.logAccion("Error al obtener usuarios no aceptados: " + e.getMessage(), className);
            return List.of();
        }
    }

    public UserDTO login(UserDTO loginDTO) {
        try {

            if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
                logger.logAccion("Intento de login con campos faltantes", className);
                return null;
            }

            Optional<User> userOpt = userDAO.findByUsername(loginDTO.getUsername());

            if (userOpt.isEmpty()) {
                logger.logAccion("Intento de login con usuario inexistente: " + loginDTO.getUsername(), className);
                return null;
            }
            User user = userOpt.get();

            if (!user.getPassword().equals(loginDTO.getPassword())) {
                logger.logAccion("Intento de login con contraseña incorrecta para: " + loginDTO.getUsername(), className);
                return null;
            }

            if (!user.getAccepted()) {
                logger.logAccion("Intento de login de usuario no aceptado: " + loginDTO.getUsername(), className);
                return null;
            }

            logger.logAccion("Login exitoso para: " + loginDTO.getUsername(), className);
            return UserMapper.userToDTO(user);

        } catch (SQLException e) {
            logger.logAccion("Error en login: " + e.getMessage(), className);
            return null;
        }
    }

}
