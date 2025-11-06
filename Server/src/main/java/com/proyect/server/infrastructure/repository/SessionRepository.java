package com.proyect.server.infrastructure.repository;

import com.proyect.server.infrastructure.entity.ESession;
import com.proyect.server.infrastructure.entity.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<ESession, String> {

    List<ESession> findByUser(EUser user);

    @Query("SELECT s FROM ESession s WHERE s.status = 'CONNECTED'")
    List<ESession> findActiveSessions();

    @Query("SELECT s FROM ESession s WHERE s.status = 'DISCONNECTED'")
    List<ESession> findInactiveSessions();

    @Query("SELECT COUNT(s) FROM ESession s WHERE s.user.id = :userId AND s.status = 'CONNECTED'")
    Long countActiveSessionsByUser(Integer userId);
}
