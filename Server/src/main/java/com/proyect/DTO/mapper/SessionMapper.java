package com.proyect.DTO.mapper;

import com.proyect.domain.Session;
import com.proyect.DTO.*;
import com.proyect.domain.User;

public class SessionMapper {

    public static SessionDTO sessionToDTO(Session session) {
        if (session == null) return null;

        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setUserId(session.getUser() != null ? session.getUser().getId() : 0);
        dto.setIp(session.getIp());
        dto.setStatus(session.getStatus());
        dto.setConnectionTime(session.getConnectionTime());
        dto.setDisconnectionTime(session.getDisconnectionTime());

        return dto;
    }

    // Método original (sin User)
    public static Session dtoToSession(SessionDTO dto) {
        if (dto == null) return null;

        Session session = new Session();
        session.setId(dto.getId());
        session.setIp(dto.getIp());
        session.setStatus(dto.getStatus());
        session.setConnectionTime(dto.getConnectionTime());
        session.setDisconnectionTime(dto.getDisconnectionTime());

        // Nota: El usuario no se establece aquí
        return session;
    }

    // Nuevo método sobrecargado que acepta User
    public static Session dtoToSession(SessionDTO dto, User user) {
        if (dto == null) return null;

        Session session = new Session();
        session.setId(dto.getId());
        session.setIp(dto.getIp());
        session.setStatus(dto.getStatus());
        session.setConnectionTime(dto.getConnectionTime());
        session.setDisconnectionTime(dto.getDisconnectionTime());

        // Establecer el usuario
        session.setUser(user);

        return session;
    }
}
