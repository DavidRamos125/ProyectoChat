package com.proyectofinal.application;

import com.proyectofinal.util.Config;

import java.io.IOException;
import java.net.Socket;

public class SocketHandler {
    private String host;
    private int port;
    private Socket socket;
    private MessageHandler messageHandler;
    private boolean connected = false;

    public SocketHandler() {
        this.host = Config.getProperty("host");
        this.port = Integer.parseInt(Config.getProperty("port"));
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            messageHandler = new MessageHandler(socket);
            connected = true;
            System.out.println("Conectado al servidor: " + host + ":" + port);

            // Ejecutar recepción en un hilo aparte
            new Thread(() -> messageHandler.listen()).start();

            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            connected = false;
            return false;
        }
    }

    public void sendMessage(String message) {
        if (connected && messageHandler != null) {
            messageHandler.send(message);
        } else {
            System.err.println("No hay conexión activa con el servidor.");
        }
    }

    public void disconnect() {
        try {
            connected = false;
            if (messageHandler != null) {
                messageHandler.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Desconectado del servidor.");
        } catch (IOException e) {
            System.err.println("Error al desconectarse: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
