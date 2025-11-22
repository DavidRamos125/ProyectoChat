package com.proyect.persistence.DAO;

import com.proyect.domain.Session;
import com.proyect.domain.User;
import com.proyect.factory.InternalFactory;

import java.sql.*;
import java.util.Optional;

public class SessionDAO {

    private Connection con;

    public SessionDAO() {
    }

    public void insert(Session s) throws SQLException {
        setConnection();
        String sql = "INSERT INTO sessions (id, user_id, ip, connection_time, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, s.getId());
            ps.setInt(2, s.getUser().getId());
            ps.setString(3, s.getIp());
            ps.setTimestamp(4, InternalFactory.getTimestamp());
            ps.setString(5, "online");
            ps.executeUpdate();
        }
    }


    public Optional<Session> findById(String id) throws SQLException {
        setConnection();
        String sql = "SELECT s.id AS session_id, s.ip, s.connection_time, s.status, s.disconnection_time, " +
                "u.id AS user_id, u.username, u.password, u.accepted " +
                "FROM sessions s " +
                "JOIN users u ON s.user_id = u.id " +
                "WHERE s.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                User user = InternalFactory.getUser(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("accepted")
                );

                Session session = InternalFactory.getSession(
                        rs.getString("session_id"),
                        user,
                        rs.getString("ip"),
                        rs.getTimestamp("connection_time"),
                        rs.getTimestamp("disconnection_time")
                );
                return Optional.of(session);
            }
        }
    }


    public void desactivate(String id) throws SQLException {
        setConnection();
        String sql = "UPDATE sessions SET status = 'offline', disconnection_time = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, InternalFactory.getTimestamp());
            ps.setString(2, id);
            ps.executeUpdate();
        }
    }
    
    public void setConnection(){
        con = InternalFactory.getConnection();
    }
}

