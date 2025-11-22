package com.proyect.persistence.DAO;

import com.proyect.domain.TextContent;
import com.proyect.factory.InternalFactory;

import java.sql.*;

public class TextContentDAO {

    public TextContent findById(String messageId) throws SQLException {
        String sql = "SELECT message_id, text FROM text_contents WHERE message_id = ?";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TextContent tc = new TextContent();
                    tc.setText(rs.getString("text"));
                    return tc;
                }
            }
        }
        return null;
    }

    public void insert(String messageId, TextContent content) throws SQLException {
        String sql = "INSERT INTO text_contents (message_id, text) VALUES (?, ?)";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.setString(2, content.getText());

            ps.executeUpdate();
        }
    }

    public void delete(String messageId) throws SQLException {
        String sql = "DELETE FROM text_contents WHERE message_id = ?";

        try (Connection con = InternalFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, messageId);
            ps.executeUpdate();
        }
    }
}
