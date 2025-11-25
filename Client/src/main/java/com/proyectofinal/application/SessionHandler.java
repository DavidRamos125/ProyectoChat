package com.proyectofinal.application;

import com.proyectofinal.DTO.UserDTO;
import com.proyectofinal.DTO.MessageDTO;
import com.proyectofinal.util.JSONBuilder;
import com.proyectofinal.util.JSONUtil;

import java.io.*;
import java.net.Socket;

public class SessionHandler {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean listening = true;
    private UserDTO currentUser;

    private static SessionHandler instance;

    public static SessionHandler getInstance(Socket socket) {
        if (instance == null) {
            instance = new SessionHandler(socket);
        }
        return instance;
    }

    private SessionHandler(Socket socket) {
        try {
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error al inicializar MessageHandler: " + e.getMessage());
        }
    }

    public void send(String message) {
        if (output != null) {
            output.println(message);
            System.out.println("📤 Mensaje enviado: " + message);
        }
    }
    
    public void listen() {
        new Thread(() -> {
            try {
                String line;
                while (listening && (line = input.readLine()) != null) {
                    processMessage(line);
                }
            } catch (IOException e) {
                System.err.println("Error al recibir mensaje: " + e.getMessage());
            } finally {
                close();
            }
        }).start();
    }

    // --------------------------------------------------------------
    //  LOGIN  →  action + user (solo username y password)
    // --------------------------------------------------------------
    public void sendLogin(String username, String password) {
        UserDTO dto = new UserDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        String loginMessage = JSONBuilder.create()
                .add("action", "LOGIN")
                .add("user", dto)               // <-- DTO bajo la clave "user"
                .build();
        send(loginMessage);
    }

    // --------------------------------------------------------------
    //  REGISTER
    // --------------------------------------------------------------
    public void sendRegister(String username, String password) {
        UserDTO dto = new UserDTO();
        dto.setUsername(username);
        dto.setPassword(password);

        String registerMessage = JSONBuilder.create()
                .add("action", "REGISTER")
                .add("user", dto)               // <-- DTO bajo la clave "user"
                .build();
        send(registerMessage);
    }

    // --------------------------------------------------------------
    //  LOGOUT (solo action)
    // --------------------------------------------------------------
    public void sendLogout() {
        String logoutMessage = JSONBuilder.create()
                .add("action", "LOGOUT")
                .build();
        send(logoutMessage);
        currentUser = null;
    }

    // --------------------------------------------------------------
    //  RECEPCIÓN DE MENSAJES (uso de getDTO)
    // --------------------------------------------------------------
    private void processMessage(String message) {
        System.out.println("📥 Mensaje recibido: " + message);
        try {
            String action = JSONUtil.getProperty(message, "action");
            if (action == null) {
                System.err.println("Mensaje sin acción: " + message);
                return;
            }

            switch (action) {
                case "LOGIN_SUCCESS":
                    handleLoginSuccess(message);
                    break;
                case "NEW_MESSAGE":
                    handleNewMessage(message);
                    break;
                case "USER_CONNECTED":
                    handleUserConnected(message);
                    break;
                case "USER_DISCONNECTED":
                    handleUserDisconnected(message);
                    break;
                case "SERVER_DISCONNECTED":
                    // TODO
                    break;
                default:
                    System.out.println("Acción no manejada: " + action);
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------
    //  LOGIN_SUCCESS → contiene "user" (UserDTO) + otros campos (sessionId,…)
    // --------------------------------------------------------------
    private void handleLoginSuccess(String message) {
        try {
            UserDTO user = JSONUtil.JSONToObject(message, UserDTO.class);
            this.currentUser = user;

            String sessionId = JSONUtil.getProperty(message, "sessionId");
            System.out.println("✅ Login OK – Usuario: " +
                    (user != null ? user.getUsername() : "unknown") +
                    ", Sesión: " + sessionId);
        } catch (Exception e) {
            System.err.println("Error procesando login success: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------
    //  NEW_MESSAGE → contiene "message" (MessageDTO) + "sender"
    // --------------------------------------------------------------
    private void handleNewMessage(String message) {
        try {
            MessageDTO msg = JSONUtil.JSONToObject(message, MessageDTO.class);
            String sender = JSONUtil.getProperty(message, "sender");
            if (msg != null) {
                System.out.println("💬 Nuevo mensaje de " + sender + ": " +
                        (msg.getTextContent() != null ? msg.getTextContent() : "Archivo"));
            }
        } catch (Exception e) {
            System.err.println("Error procesando nuevo mensaje: " + e.getMessage());
        }
    }

    private void handleUserConnected(String message) {
        String username = JSONUtil.getProperty(message, "username");
        System.out.println("👤 Usuario conectado: " + username);
    }

    private void handleUserDisconnected(String message) {
        String username = JSONUtil.getProperty(message, "username");
        System.out.println("👤 Usuario desconectado: " + username);
    }


    public void close() {
        listening = false;
        try {
            sendLogout();
            output.flush();
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar MessageHandler: " + e.getMessage());
        }
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed() && listening;
    }
}
