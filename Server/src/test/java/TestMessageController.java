import com.proyect.DTO.mapper.UserMapper;
import com.proyect.controller.MessageController;
import com.proyect.controller.UserController;
import com.proyect.DTO.*;
import com.proyect.factory.InternalFactory;
import com.proyect.persistence.DAO.SessionDAO;
import com.proyect.domain.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Prueba rápida del MessageController.
 * Se crean usuarios, sesiones, se envía un mensaje y se recuperan.
 */
public class TestMessageController {

    public static void main(String[] args) throws Exception {
        System.out.println("=== INICIANDO PRUEBAS DE MESSAGECONTROLLER ===\n");

        // -----------------------------------------------------------------
        // 0️⃣  Preparar el entorno (UserController para crear usuarios)
        // -----------------------------------------------------------------
        UserController userCtrl = new UserController();

        // Crear dos usuarios (juan y maria) y aprobarlos
        UserDTO juanDTO = new UserDTO();
        juanDTO.setUsername("juan3e1"+UUID.randomUUID());
        juanDTO.setPassword("pass12w3");
        UserDTO juanCreated = userCtrl.createUser(juanDTO);
        userCtrl.approveUser(juanCreated.getId());

        UserDTO mariaDTO = new UserDTO();
        mariaDTO.setUsername("maria"+UUID.randomUUID());
        mariaDTO.setPassword("pass4t56");
        UserDTO mariaCreated = userCtrl.createUser(mariaDTO);
        userCtrl.approveUser(mariaCreated.getId());

        System.out.println("Usuarios creados y aprobados:");
        System.out.println(juanCreated.getUsername()+" (id=" + juanCreated.getId() + ")");
        System.out.println(mariaCreated.getUsername() + mariaCreated.getId() + ")\n");

        // -----------------------------------------------------------------
        // 1️⃣  Crear sesiones (una para cada usuario)
        // -----------------------------------------------------------------
        SessionDAO sessionDAO = InternalFactory.getSessionDAO();

        // Sesión de juan (emisor)
        Session sessJuan = new Session();
        sessJuan.setId("sess-juan-" + UUID.randomUUID());
        sessJuan.setUser(UserMapper.dtoToUser(juanCreated));
        sessJuan.setIp("127.0.0.1");
        sessJuan.setStatus("online");
        sessJuan.setConnectionTime(Timestamp.valueOf(LocalDateTime.now()));
        sessionDAO.insert(sessJuan);

        // Sesión de maria (receptora)
        Session sessMaria = new Session();
        sessMaria.setId("sess-maria-" + UUID.randomUUID());
        sessMaria.setUser(UserMapper.dtoToUser(mariaCreated));
        sessMaria.setIp("127.0.0.2");
        sessMaria.setStatus("online");
        sessMaria.setConnectionTime(Timestamp.valueOf(LocalDateTime.now()));
        sessionDAO.insert(sessMaria);

        System.out.println("Sesiones creadas:");
        System.out.println(" - juan  : " + sessJuan.getId());
        System.out.println(" - maria : " + sessMaria.getId() + "\n");

        // -----------------------------------------------------------------
        // 2️⃣  Instanciar el MessageController
        // -----------------------------------------------------------------
        MessageController msgCtrl = new MessageController();

        // -----------------------------------------------------------------
        // 3️⃣  Enviar un mensaje de texto usando el método de conveniencia
        // -----------------------------------------------------------------
        System.out.println("Enviando mensaje de texto (juan → maria)...");
        MessageDTO enviado = msgCtrl.sendTextMessage(
                juanCreated.getId(),
                mariaCreated.getId(),
                sessJuan.getId(),
                "¡Hola Maria! Este es un mensaje de prueba.");

        if (enviado == null) {
            System.out.println("❌  Error al enviar el mensaje.");
            return;
        }
        System.out.println("✅  Mensaje enviado, ID generado: " + enviado.getId());

        // -----------------------------------------------------------------
        // 4️⃣  Obtener el mensaje por su ID
        // -----------------------------------------------------------------
        System.out.println("\nRecuperando mensaje por ID...");
        MessageDTO recuperado = msgCtrl.getMessageById(enviado.getId());
        if (recuperado != null) {
            System.out.println("✅  Mensaje recuperado:");
            System.out.println("    ID          : " + recuperado.getId());
            System.out.println("    Remitente   : " + recuperado.getSender().getUsername());
            System.out.println("    Destinatario: " + recuperado.getReceiver().getUsername());
            System.out.println("    Tipo        : " + recuperado.getType());
            System.out.println("    Texto       : " + recuperado.getTextContent());
        } else {
            System.out.println("❌  No se encontró el mensaje.");
        }

        // -----------------------------------------------------------------
        // 5️⃣  Listar todos los mensajes del usuario “juan”
        // -----------------------------------------------------------------
        System.out.println("\nListando mensajes de \"juan\" (id=" + juanCreated.getId() + ")...");
        List<MessageDTO> mensajesDeJuan = msgCtrl.getMessagesByUser(juanCreated.getId());
        System.out.println("🔎  Encontrados " + mensajesDeJuan.size() + " mensaje(s).");
        for (MessageDTO m : mensajesDeJuan) {
            System.out.println("- ID:" + m.getId()
                    + " | De:" + m.getSender().getUsername()
                    + " → Para:" + m.getReceiver().getUsername()
                    + " | Tipo:" + m.getType()
                    + " | Texto:" + (m.getTextContent() != null
                    ? (m.getTextContent().length() > 30
                    ? m.getTextContent().substring(0, 30) + "..."
                    : m.getTextContent())
                    : "(archivo)"));
        }

        System.out.println("\n=== FIN DE PRUEBAS DE MESSAGECONTROLLER ===");
    }
}
