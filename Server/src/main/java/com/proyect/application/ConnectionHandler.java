package com.proyect.application;

import com.proyect.controller.UserController;
import com.proyect.DTO.SessionDTO;
import com.proyect.DTO.UserDTO;
import com.proyect.factory.ExternalFactory;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private final Logger logger;
    private BufferedReader in;
    private PrintWriter out;
    private SessionDTO currentSession;
    private UserDTO currentUser;
    private boolean loggedIn = false;
    private String clientAddress;

    private CommunicationHandler communicationHandler;
    private UserController userController;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        this.server = ExternalFactory.getServer();
        this.logger = ExternalFactory.getLogger();
        this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.userController = ExternalFactory.getUserController();
        this.communicationHandler = new CommunicationHandler(this);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            logger.logAccion("Conexión establecida desde: " + clientAddress, this.getClass().getSimpleName());

            String message;
            while ((message = in.readLine()) != null && !socket.isClosed()) {
                communicationHandler.handleMessage(message);
            }

        } catch (IOException e) {
            logger.logAccion("Error en conexión " + clientAddress + ": " + e.getMessage(), this.getClass().getSimpleName());
        } finally {
            close();
        }
    }

    public boolean createUserSession(UserDTO user) {
        try {

            this.currentSession = new SessionDTO();
            this.currentSession.setUserId(user.getId());
            this.currentSession.setId(generateSessionId(user.getId()));
            this.currentSession.setIp(clientAddress.split(":")[0]);
            this.currentSession.setStatus("online");
            this.currentSession.setConnectionTime(new Timestamp(System.currentTimeMillis()));

            this.currentUser = user;
            this.loggedIn = true;

            logger.logAccion("Sesión creada para usuario: " + user.getUsername(), this.getClass().getSimpleName());
            return true;

        } catch (Exception e) {
            logger.logAccion("Error creando sesión: " + e.getMessage(), this.getClass().getSimpleName());
            return false;
        }
    }

    public void logout() {
        if (currentSession != null) {
            currentSession.setStatus("offline");
            currentSession.setDisconnectionTime(new Timestamp(System.currentTimeMillis()));

            logger.logAccion("Sesión cerrada para usuario: "
                            + (currentUser != null ? currentUser.getUsername() : "unknown"),
                    this.getClass().getSimpleName());
        }
        this.currentSession = null;
        this.currentUser = null;
        this.loggedIn = false;
    }

    public void sendMessage(String message) {
        if (out != null && !socket.isClosed()) {
            out.println(message);
        }
    }

    public void close() {
        logout();
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            server.removeSession(clientAddress);
            logger.logAccion("Conexión cerrada: " + clientAddress, this.getClass().getSimpleName());
        } catch (IOException e) {
            logger.logAccion("Error al cerrar conexión: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    private String generateSessionId(int id) {
        return "sess_" + id + "-" + (int) (Math.random() * 1000);
    }

    public SessionDTO getCurrentSession() {
        return currentSession;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
