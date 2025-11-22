package com.proyect.persistence.DAO;

import com.proyect.domain.*;
import com.proyect.domain.interfaces.Content;
import com.proyect.factory.InternalFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    private Connection con;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    public MessageDAO() {
        userDAO = InternalFactory.getUserDAO();
        sessionDAO = InternalFactory.getSessionDAO();
    }

    public void insert(Message m) throws SQLException {
        String sql = """
            INSERT INTO messages (sender_id, receiver_id, session_sender_id, content_type, date)
            VALUES (?, ?, ?, ?, ?)
        """;

        setConnection();

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, m.getSender().getId());
            ps.setInt(2, m.getReceiver().getId());
            ps.setString(3, m.getSessionSender().getId());
            ps.setString(4, m.getContent().getType().name());
            ps.setTimestamp(5, Timestamp.valueOf(m.getDate().toLocalDateTime()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }
        }

        // Insertar contenido según el tipo
        if (m.getContent().getType() == ContentType.TEXT) {
            insertTextContent(m.getId(), (TextContent) m.getContent());
        } else {
            insertFileContent(m.getId(), (FileContent) m.getContent());
        }

        // Insertar sesiones receptoras
        insertReceivers(m.getId(), m.getSessionsReceived());
    }

    private void insertTextContent(int messageId, TextContent content) throws SQLException {
        String sql = "INSERT INTO text_contents (message_id, text) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setString(2, content.getText());
            ps.executeUpdate();
        }
    }

    private void insertFileContent(int messageId, FileContent content) throws SQLException {
        String sql = "INSERT INTO file_contents (message_id, name, size, data) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ps.setString(2, content.getName());
            ps.setLong(3, content.getSize());
            ps.setBytes(4, content.getData());
            ps.executeUpdate();
        }
    }

    private void insertReceivers(int messageId, List<Session> sessions) throws SQLException {
        if (sessions == null || sessions.isEmpty()) return;

        String sql = "INSERT INTO message_receivers (message_id, session_id) VALUES (?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Session s : sessions) {
                ps.setInt(1, messageId);
                ps.setString(2, s.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public Message findById(int id) throws SQLException {
        String sql = """
        SELECT 
            m.id AS msg_id,
            m.date AS msg_date,
            m.content_type,
            u1.id AS sender_id,
            u1.username AS sender_username,
            u1.password AS sender_password,
            u1.accepted AS sender_accepted,
            u2.id AS receiver_id,
            u2.username AS receiver_username,
            u2.password AS receiver_password,
            u2.accepted AS receiver_accepted,
            s.id AS session_sender_id,
            s.ip AS session_ip,
            s.connection_time AS session_conn_time,
            s.disconnection_time AS session_disc_time,
            s.status AS session_status
        FROM messages m
        JOIN users u1 ON m.sender_id = u1.id
        JOIN users u2 ON m.receiver_id = u2.id
        JOIN sessions s ON m.session_sender_id = s.id
        WHERE m.id = ?
    """;

        setConnection();
        Message msg = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    msg = new Message();
                    msg.setId(rs.getInt("msg_id"));
                    msg.setDate(rs.getTimestamp("msg_date"));

                    // Sender user
                    User sender = new User();
                    sender.setId(rs.getInt("sender_id"));
                    sender.setUsername(rs.getString("sender_username"));
                    sender.setPassword(rs.getString("sender_password"));
                    sender.setAccepted(rs.getBoolean("sender_accepted"));
                    msg.setSender(sender);

                    // Receiver user
                    User receiver = new User();
                    receiver.setId(rs.getInt("receiver_id"));
                    receiver.setUsername(rs.getString("receiver_username"));
                    receiver.setPassword(rs.getString("receiver_password"));
                    receiver.setAccepted(rs.getBoolean("receiver_accepted"));
                    msg.setReceiver(receiver);

                    // Session sender
                    Session ss = new Session();
                    ss.setId(rs.getString("session_sender_id"));
                    ss.setIp(rs.getString("session_ip"));
                    ss.setConnectionTime(rs.getTimestamp("session_conn_time"));
                    ss.setDisconnectionTime(rs.getTimestamp("session_disc_time"));
                    ss.setStatus(rs.getString("session_status"));
                    ss.setUser(sender);
                    msg.setSessionSender(ss);

                    // Content
                    msg.setContent(loadContent(id, rs.getString("content_type")));

                    // Sessions received
                    msg.setSessionsReceived(loadReceivers(id));
                }
            }
        }

        return msg;
    }

    private Content loadContent(int messageId, String type) throws SQLException {
        if ("TEXT".equals(type)) {
            String sql = "SELECT text FROM text_contents WHERE message_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, messageId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        TextContent tc = new TextContent();
                        tc.setText(rs.getString("text"));
                        return tc;
                    }
                }
            }
        } else if ("FILE".equals(type)) {
            String sql = "SELECT name, size, data FROM file_contents WHERE message_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, messageId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        FileContent fc = new FileContent();
                        fc.setName(rs.getString("name"));
                        fc.setData(rs.getBytes("data"));
                        return fc;
                    }
                }
            }
        }
        return null;
    }

    private List<Session> loadReceivers(int messageId) throws SQLException {
        String sql = """
        SELECT s.*, u.id as user_id, u.username, u.password, u.accepted
        FROM message_receivers mr
        JOIN sessions s ON mr.session_id = s.id
        JOIN users u ON s.user_id = u.id
        WHERE mr.message_id = ?
    """;

        List<Session> list = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Session s = new Session();
                    s.setId(rs.getString("id"));
                    s.setIp(rs.getString("ip"));
                    s.setConnectionTime(rs.getTimestamp("connection_time"));
                    s.setDisconnectionTime(rs.getTimestamp("disconnection_time"));
                    s.setStatus(rs.getString("status"));

                    // Cargar usuario de la sesión
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setAccepted(rs.getBoolean("accepted"));
                    s.setUser(user);

                    list.add(s);
                }
            }
        }

        return list;
    }

    public List<Message> getMessagesByUser(int userId) throws SQLException {
        String sql = """
        SELECT 
            m.id AS msg_id,
            m.date AS msg_date,
            m.content_type,
            u1.id AS sender_id,
            u1.username AS sender_username,
            u1.password AS sender_password,
            u1.accepted AS sender_accepted,
            u2.id AS receiver_id,
            u2.username AS receiver_username,
            u2.password AS receiver_password,
            u2.accepted AS receiver_accepted,
            s.id AS session_sender_id,
            s.ip AS session_ip,
            s.connection_time AS session_conn_time,
            s.disconnection_time AS session_disc_time,
            s.status AS session_status
        FROM messages m
        JOIN users u1 ON m.sender_id = u1.id
        JOIN users u2 ON m.receiver_id = u2.id
        JOIN sessions s ON m.session_sender_id = s.id
        WHERE ? IN (m.sender_id, m.receiver_id)
        ORDER BY m.date ASC
    """;

        setConnection();
        List<Message> messages = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message();
                    msg.setId(rs.getInt("msg_id"));
                    msg.setDate(rs.getTimestamp("msg_date"));

                    // Sender user
                    User sender = new User();
                    sender.setId(rs.getInt("sender_id"));
                    sender.setUsername(rs.getString("sender_username"));
                    sender.setPassword(rs.getString("sender_password"));
                    sender.setAccepted(rs.getBoolean("sender_accepted"));
                    msg.setSender(sender);

                    // Receiver user
                    User receiver = new User();
                    receiver.setId(rs.getInt("receiver_id"));
                    receiver.setUsername(rs.getString("receiver_username"));
                    receiver.setPassword(rs.getString("receiver_password"));
                    receiver.setAccepted(rs.getBoolean("receiver_accepted"));
                    msg.setReceiver(receiver);

                    // Session sender
                    Session ss = new Session();
                    ss.setId(rs.getString("session_sender_id"));
                    ss.setIp(rs.getString("session_ip"));
                    ss.setConnectionTime(rs.getTimestamp("session_conn_time"));
                    ss.setDisconnectionTime(rs.getTimestamp("session_disc_time"));
                    ss.setStatus(rs.getString("session_status"));
                    ss.setUser(sender);
                    msg.setSessionSender(ss);

                    // Content
                    msg.setContent(loadContent(msg.getId(), rs.getString("content_type")));

                    // Sessions received (cargar después para cada mensaje)
                    msg.setSessionsReceived(loadReceivers(msg.getId()));

                    messages.add(msg);
                }
            }
        }

        return messages;
    }

    private void setConnection() {
        try {
            if (con == null || con.isClosed()) {
                con = InternalFactory.getConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}
