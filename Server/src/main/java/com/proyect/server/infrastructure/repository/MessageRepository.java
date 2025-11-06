package com.proyect.server.infrastructure.repository;

import com.proyect.server.infrastructure.entity.EMessage;
import com.proyect.server.infrastructure.entity.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<EMessage, Integer> {

    List<EMessage> findBySender(EUser sender);
    List<EMessage> findByReceiver(EUser receiver);

    // Mensajes enviados por IP (para API REST)
    @Query("SELECT m FROM EMessage m WHERE m.sender.id = :userId")
    List<EMessage> findMessagesSentByUser(Integer userId);

    // Mensajes recibidos por IP (para API REST)
    @Query("SELECT m FROM EMessage m WHERE m.receiver.id = :userId")
    List<EMessage> findMessagesReceivedByUser(Integer userId);

    // Clasificar por tipo (TEXT o FILE)
    List<EMessage> findByContentType(EMessage.ContentType contentType);

    // Últimos mensajes enviados (para informes)
    @Query("SELECT m FROM Message m ORDER BY m.dateSent DESC")
    List<EMessage> findLatestMessages();
}
