package com.proyect.server.domain.DAO;

import com.proyect.server.domain.entity.FileContent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {

    private final Connection connection;

    public FileDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Guarda un archivo en la base de datos.
     */
    public boolean save(FileContent file) {
        String sql = "INSERT INTO files (name,  data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, file.getName());
            stmt.setLong(2, file.getSize());
            stmt.setInt(3, file.getOwner().getId());
            stmt.setBytes(4, file.getData());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un archivo por su ID (incluye datos binarios).
     */
    public FileContent findById(int fileId, User owner) {
        String sql = "SELECT name, size, data FROM files WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, fileId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                long size = rs.getLong("size");
                byte[] data = rs.getBytes("data");

                FileContent file = new FileContent(name, owner, data);
                // el tamaño se calcula internamente en el constructor
                return file;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener archivo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista los archivos subidos por un usuario (sin datos binarios).
     */
    public List<FileContent> findByOwner(User owner) {
        List<FileContent> files = new ArrayList<>();
        String sql = "SELECT id, name, size FROM files WHERE owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, owner.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                long size = rs.getLong("size");
                // no cargamos data para no saturar memoria
                FileContent file = new FileContent(name, owner, new byte[0]);
                file.setData(new byte[(int) size]); // placeholder para reflejar el tamaño
                files.add(file);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error listando archivos: " + e.getMessage());
        }
        return files;
    }

    /**
     * Cuenta cuántos archivos ha subido un usuario.
     */
    public int countByOwner(User owner) {
        String sql = "SELECT COUNT(*) FROM files WHERE owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, owner.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("❌ Error contando archivos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Lista todos los archivos ordenados por tamaño (para informes administrativos).
     */
    public List<FileContent> findAllOrderedBySize() {
        List<FileContent> files = new ArrayList<>();
        String sql = """
            SELECT f.name, f.size, f.data, u.id AS owner_id, u.username 
            FROM files f
            JOIN users u ON f.owner_id = u.id
            ORDER BY f.size DESC
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                long size = rs.getLong("size");
                byte[] data = rs.getBytes("data");

                User owner = new User(rs.getInt("owner_id"), rs.getString("username"));
                FileContent file = new FileContent(name, owner, data);
                files.add(file);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error listando archivos por tamaño: " + e.getMessage());
        }
        return files;
    }
}
