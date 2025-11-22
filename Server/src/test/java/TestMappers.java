import com.proyect.DTO.*;
import com.proyect.DTO.mapper.*;
import com.proyect.domain.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestMappers {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DE MAPEADORES CORREGIDOS ===\n");

        // Prueba la conversión completa DTO -> Message -> DTO
        testMessageConversionWithSessions();

        System.out.println("=== PRUEBAS COMPLETADAS ===");
    }

    private static void testMessageConversionWithSessions() {
        System.out.println("--- Probando MessageMapper con Sesiones (versión corregida) ---");

        // Crear usuarios
        User sender = createUser(1, "juan", "pass123", true);
        User receiver = createUser(2, "maria", "pass456", true);

        // Crear sesión emisora
        Session sessionSender = createSession("session-juan", sender, "192.168.1.100", "online");

        // Crear sesiones receptoras
        Session sessionReceiver1 = createSession("session-maria1", receiver, "192.168.1.101", "online");
        Session sessionReceiver2 = createSession("session-maria2", receiver, "192.168.1.102", "online");
        List<Session> sessionsReceived = Arrays.asList(sessionReceiver1, sessionReceiver2);

        // Crear mensaje de texto
        Message originalMessage = new Message();
        originalMessage.setId(100);
        originalMessage.setSender(sender);
        originalMessage.setReceiver(receiver);
        originalMessage.setSessionSender(sessionSender);
        originalMessage.setSessionsReceived(sessionsReceived);
        originalMessage.setDate(Timestamp.valueOf(LocalDateTime.now()));

        TextContent textContent = new TextContent();
        textContent.setText("Mensaje de prueba con sesiones");
        originalMessage.setContent(textContent);

        System.out.println("Message original:");
        System.out.println("  Sender: " + originalMessage.getSender().getUsername());
        System.out.println("  SessionSender user: " +
                (originalMessage.getSessionSender().getUser() != null ?
                        originalMessage.getSessionSender().getUser().getUsername() : "null"));
        System.out.println("  SessionsReceived count: " + originalMessage.getSessionsReceived().size());
        System.out.println("  First session user: " +
                (originalMessage.getSessionsReceived().get(0).getUser() != null ?
                        originalMessage.getSessionsReceived().get(0).getUser().getUsername() : "null"));

        // Convertir a DTO
        MessageDTO messageDTO = MessageMapper.messageToDTO(originalMessage);
        System.out.println("\nMessageDTO creado:");
        System.out.println("  Sender: " + messageDTO.getSender().getUsername());
        System.out.println("  SessionSender userId: " + messageDTO.getSessionSender().getUserId());
        System.out.println("  SessionsReceived count: " + messageDTO.getSessionsReceived().size());

        // Convertir de vuelta a Message
        Message reconstructedMessage = MessageMapper.dtoToMessage(messageDTO);
        System.out.println("\nMessage reconstruido:");
        System.out.println("  Sender: " + reconstructedMessage.getSender().getUsername());
        System.out.println("  SessionSender user: " +
                (reconstructedMessage.getSessionSender().getUser() != null ?
                        reconstructedMessage.getSessionSender().getUser().getUsername() : "null"));
        System.out.println("  SessionsReceived count: " + reconstructedMessage.getSessionsReceived().size());
        System.out.println("  First session user: " +
                (reconstructedMessage.getSessionsReceived().get(0).getUser() != null ?
                        reconstructedMessage.getSessionsReceived().get(0).getUser().getUsername() : "null"));

        // Verificar que los usuarios están correctamente asignados
        boolean sessionSenderUserCorrect = reconstructedMessage.getSessionSender().getUser() != null &&
                reconstructedMessage.getSessionSender().getUser().getId() == sender.getId();

        boolean sessionReceiverUserCorrect = reconstructedMessage.getSessionsReceived().get(0).getUser() != null &&
                reconstructedMessage.getSessionsReceived().get(0).getUser().getId() == receiver.getId();

        System.out.println("\nVerificaciones:");
        System.out.println("  SessionSender tiene el usuario correcto: " + sessionSenderUserCorrect);
        System.out.println("  SessionReceived tiene el usuario correcto: " + sessionReceiverUserCorrect);
    }

    // Métodos auxiliares (los mismos de antes)
    private static User createUser(int id, String username, String password, boolean accepted) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setAccepted(accepted);
        return user;
    }

    private static Session createSession(String id, User user, String ip, String status) {
        Session session = new Session();
        session.setId(id);
        session.setUser(user);
        session.setIp(ip);
        session.setStatus(status);
        session.setConnectionTime(Timestamp.valueOf(LocalDateTime.now()));
        return session;
    }
}
