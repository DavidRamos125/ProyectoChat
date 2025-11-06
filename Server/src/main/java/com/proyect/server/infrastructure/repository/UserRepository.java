package com.proyect.server.infrastructure.repository;

import com.proyect.server.infrastructure.entity.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<EUser, Integer> {

    Optional<EUser> findByUsername(String username);

    List<EUser> findByAccepted(boolean accepted);

    // Usuario con más mensajes enviados TO-DO ELIMINAR COMENTARIOS
    @Query("SELECT u FROM EUser u JOIN u.messagesSent m GROUP BY u ORDER BY COUNT(m) DESC")
    List<EUser> findTopUsersByMessagesSent();

    // Contar mensajes enviados por un usuario
    @Query("SELECT COUNT(m) FROM EMessage m WHERE m.sender.id = :userId")
    Long countMessagesSentByUser(Integer userId);

    // Contar mensajes recibidos por un usuario
    @Query("SELECT COUNT(m) FROM EMessage m WHERE m.receiver.id = :userId")
    Long countMessagesReceivedByUser(Integer userId);

    // Usuarios conectados actualmente
    @Query("SELECT DISTINCT s.user FROM ESession s WHERE s.status = 'CONNECTED'")
    List<EUser> findConnectedUsers();

    // Usuarios desconectados
    @Query("SELECT DISTINCT s.user FROM ESession s WHERE s.status = 'DISCONNECTED'")
    List<EUser> findDisconnectedUsers();
}
