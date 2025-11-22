package com.proyect.persistence.DAO;

import com.proyect.domain.User;
import com.proyect.factory.InternalFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private Connection con;

    public UserDAO() {
    }

    public void insert(User user) throws SQLException {
        con = InternalFactory.getConnection();
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        }
    }


    public Optional<User> findById(int id) throws SQLException {
        con = InternalFactory.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                User u = InternalFactory.getUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("accepted"));
                return Optional.of(u);
            }
        }
    }

    public List<User> findNonAcceptedUsers() throws SQLException {
        con = InternalFactory.getConnection();
        String sql = "SELECT * FROM users WHERE accepted = FALSE";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = InternalFactory.getUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("accepted"));
                users.add(u);
            }
        }

        return users;
    }

    public void updateStatus(int id, boolean accepted) throws SQLException {
        con = InternalFactory.getConnection();
        String sql = "UPDATE users SET accepted = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, accepted);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public Optional<User> findByUsername(String username) throws SQLException {
        con = InternalFactory.getConnection();
        String sql = "SELECT * FROM users WHERE username = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty(); // Usuario no encontrado
                }

                // Crear el usuario a partir del ResultSet
                User u = InternalFactory.getUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("accepted"));

                return Optional.of(u);
            }
        }
    }
}
