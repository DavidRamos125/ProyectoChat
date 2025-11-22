package com.proyect.persistence.DAO;


import com.proyect.domain.FileContent;
import com.proyect.factory.InternalFactory;

import java.sql.*;

public class FileContentDAO {

    public FileContent findById(String messageId) throws SQLException {
        String sql = "SELECT message_id, name, size, data FROM file_contents WHERE message_id = ?";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FileContent fc = InternalFactory.getFileContent(
                            rs.getString("message_id"),
                            rs.getString("name"),
                            rs.getBytes("data")
                    );
                    return fc;
                }
            }
        }
        return null;
    }

    public void insert(String messageId, FileContent content) throws SQLException {
        String sql = "INSERT INTO file_contents (message_id, name, size, data) VALUES (?, ?, ?, ?)";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.setString(2, content.getName());
            ps.setLong(3, content.getSize());
            ps.setBytes(4, content.getData());

            ps.executeUpdate();
        }
    }

    public void delete(String messageId) throws SQLException {
        String sql = "DELETE FROM file_contents WHERE message_id = ?";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.executeUpdate();
        }
    }
}
