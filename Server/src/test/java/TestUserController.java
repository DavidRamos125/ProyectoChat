import com.proyect.controller.UserController;
import com.proyect.DTO.UserDTO;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para la clase UserController.
 * Se ejecuta contra la base de datos configurada en InternalFactory.
 */
public class TestUserController {

    private static UserController userController;

    /**
     * Un usuario que será creado en cada test.
     * Utilizamos un UUID para que el nombre sea único y evitar colisiones entre ejecuciones.
     */
    private UserDTO testUserDTO;

    @BeforeAll
    static void beforeAll() {
        // Instanciamos una sola vez el controlador (no tiene estado mutable)
        userController = new UserController();
    }

    @BeforeEach
    void setUp() {
        // Preparar un UserDTO con datos válidos
        String uniqueName = "testUser_" + UUID.randomUUID();
        testUserDTO = new UserDTO();
        testUserDTO.setUsername(uniqueName);
        testUserDTO.setPassword("pass123");
    }


    /** Helper – buscar el id de un usuario por su username */
    private int findUserIdByUsername(String username) throws SQLException {
        try (var con = com.proyect.factory.InternalFactory.getConnection();
             var ps = con.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
                // Si no existe todavía, devolvemos -1 (el test fallará)
                return -1;
            }
        }
    }

    @Test
    @DisplayName("Crear usuario, login y aprobación")
    void testCreateLoginAndApprove() throws SQLException {
        /* ---------- 1. Crear usuario ---------- */
        UserDTO created = userController.createUser(testUserDTO);
        assertNotNull(created, "El usuario debe ser creado y devuelto");
        assertTrue(created.getId() > 0, "El ID generado debe ser mayor que cero");
        assertEquals(testUserDTO.getUsername(), created.getUsername());

        /* ---------- 2. Intentar login (debe fallar porque aún no está aceptado) ---------- */
        UserDTO loginAttempt = userController.login(testUserDTO);
        assertNull(loginAttempt, "Login debería fallar porque el usuario no está aceptado");

        /* ---------- 3. Aprobar el usuario ---------- */
        UserDTO approved = userController.approveUser(created.getId());
        assertNotNull(approved, "El usuario aprobado debe devolverse");
        assertTrue(approved.isAccepted(), "El campo accepted debe ser true");

        /* ---------- 4. Login exitoso ---------- */
        UserDTO logged = userController.login(testUserDTO);
        assertNotNull(logged, "Login debe ser exitoso después de la aprobación");
        assertEquals(approved.getId(), logged.getId());
        assertEquals(testUserDTO.getUsername(), logged.getUsername());

        /* ---------- 5. Listado de usuarios no aceptados (el creado ya no debe aparecer) ---------- */
        List<UserDTO> nonAccepted = userController.getNonAcceptedUsers();
        boolean stillPresent = nonAccepted.stream()
                .anyMatch(u -> u.getUsername().equals(testUserDTO.getUsername()));
        assertFalse(stillPresent, "El usuario aprobado no debe aparecer en la lista de no aceptados");
    }

    @Test
    @DisplayName("Rechazar usuario")
    void testRejectUser() throws SQLException {
        // Crear usuario
        UserDTO created = userController.createUser(testUserDTO);
        assertNotNull(created);

        // Rechazar (marcar accepted = false, aunque ya lo está)
        UserDTO rejected = userController.rejectUser(created.getId());
        assertNotNull(rejected);
        assertFalse(rejected.isAccepted(), "El usuario debe quedar con accepted = false");

        // Intentar login: debe fallar por contraseña correcta pero estado no aceptado
        UserDTO login = userController.login(testUserDTO);
        assertNull(login, "Login debe fallar cuando el usuario está rechazado");
    }

    @Test
    @DisplayName("Buscar usuario por ID")
    void testFindById() throws SQLException {
        UserDTO created = userController.createUser(testUserDTO);
        assertNotNull(created);

        UserDTO found = userController.getUserById(created.getId());
        assertNotNull(found);
        assertEquals(created.getUsername(), found.getUsername());
        assertEquals(created.getId(), found.getId());
    }
}
