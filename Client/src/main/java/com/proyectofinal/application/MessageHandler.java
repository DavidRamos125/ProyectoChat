package com.proyectofinal.application;

import com.proyectofinal.DTO.UserDTO;
import com.proyectofinal.DTO.MessageDTO;
import com.proyectofinal.util.JSONBuilder;
import com.proyectofinal.util.JSONUtil;

import java.io.*;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean listening = true;
    private String currentSessionId;
    private UserDTO currentUser;

    public MessageHandler(Socket socket) {
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

    public void sendLogin(String username, String password) {
        String loginMessage = JSONBuilder.create()
                .add("action", "LOGIN")
                .add("username", username)
                .add("password", password)
                .build();
        send(loginMessage);
    }

    public void sendRegister(String username, String password) {
        String registerMessage = JSONBuilder.create()
                .add("action", "REGISTER")
                .add("username", username)
                .add("password", password)
                .build();
        send(registerMessage);
    }

    public void sendTextMessage(String receiverUsername, String content) {
        String message = JSONBuilder.create()
                .add("action", "MESSAGE")
                .add("receiver", receiverUsername)
                .add("content", content)
                .add("type", "TEXT")
                .add("sessionId", currentSessionId)
                .build();
        send(message);
    }

    public void sendFileMessage(String receiverUsername, String fileName, byte[] fileData) {
        String message = JSONBuilder.create()
                .add("action", "MESSAGE")
                .add("receiver", receiverUsername)
                .add("type", "FILE")
                .add("fileName", fileName)
                .add("fileData", fileData)
                .add("sessionId", currentSessionId)
                .build();
        send(message);
    }

    public void sendLogout() {
        String logoutMessage = JSONBuilder.create()
                .add("action", "LOGOUT")
                .add("sessionId", currentSessionId)
                .build();
        send(logoutMessage);
        currentSessionId = null;
        currentUser = null;
    }

    public void requestConnectedUsers() {
        String request = JSONBuilder.create()
                .add("action", "GET_USERS")
                .add("sessionId", currentSessionId)
                .build();
        send(request);
    }

    public void listen() {
        new Thread(() -> {
            try {
                String line;
                while (listening && (line = input.readLine()) != null) {
                    processMessage(line);
                }
            } catch (IOException e) {
                System.err.println("❌ Error al recibir mensaje: " + e.getMessage());
            } finally {
                close();
            }
        }).start();
    }

    /**
     * Procesar mensaje recibido del servidor
     */
    private void processMessage(String message) {
        System.out.println("📥 Mensaje recibido: " + message);

        try {
            // Extraer acción del mensaje
            String action = JSONUtil.getProperty(message, "action");
            
            if (action == null) {
                System.err.println("Mensaje sin acción: " + message);
                return;
            }

            // Procesar según la acción
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
                    
                case "ERROR":
                    handleError(message);
                    break;
                    
                default:
                    System.out.println("Acción no manejada: " + action);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    /**
     * Manejar login exitoso
     */
    private void handleLoginSuccess(String message) {
        try {
            // Extraer datos del login
            String userJson = JSONUtil.getProperty(message, "user");
            String sessionId = JSONUtil.getProperty(message, "sessionId");
            
            if (userJson != null) {
                currentUser = JSONUtil.JSONToObject(userJson, UserDTO.class);
            }
            currentSessionId = sessionId;
            
            System.out.println("✅ Login exitoso - Usuario: " + 
                (currentUser != null ? currentUser.getUsername() : "unknown") +
                ", Sesión: " + sessionId);
                
        } catch (Exception e) {
            System.err.println("Error procesando login success: " + e.getMessage());
        }
    }

    private void handleNewMessage(String message) {
        try {
            String messageJson = JSONUtil.getProperty(message, "message");
            String sender = JSONUtil.getProperty(message, "sender");
            
            if (messageJson != null) {
                MessageDTO msg = JSONUtil.JSONToObject(messageJson, MessageDTO.class);
                System.out.println("💬 Nuevo mensaje de " + sender + ": " + 
                    (msg.getTextContent() != null ? msg.getTextContent() : "Archivo"));
            }
        } catch (Exception e) {
            System.err.println("Error procesando nuevo mensaje: " + e.getMessage());
        }
    }

    /**
     * Manejar usuario conectado
     */
    private void handleUserConnected(String message) {
        String username = JSONUtil.getProperty(message, "username");
        System.out.println("👤 Usuario conectado: " + username);
    }

    /**
     * Manejar usuario desconectado
     */
    private void handleUserDisconnected(String message) {
        String username = JSONUtil.getProperty(message, "username");
        System.out.println("👤 Usuario desconectado: " + username);
    }

    /**
     * Manejar errores
     */
    private void handleError(String message) {
        String errorMsg = JSONUtil.getProperty(message, "error");
        System.err.println("❌ Error del servidor: " + errorMsg);
    }

    /**
     * Cerrar conexión
     */
    public void close() {
        listening = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar MessageHandler: " + e.getMessage());
        }
    }

    public String getCurrentSessionId() { return currentSessionId; }
    public UserDTO getCurrentUser() { return currentUser; }
    public boolean isConnected() { return socket != null && !socket.isClosed() && listening; }
}
