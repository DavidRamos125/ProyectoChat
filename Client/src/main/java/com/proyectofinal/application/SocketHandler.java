package com.proyectofinal.application;

import com.proyectofinal.factory.ExternalFactory;
import com.proyectofinal.util.Config;

import java.io.IOException;
import java.net.Socket;

public class SocketHandler {
    private String host;
    private int port;
    private Socket socket;
    private SessionHandler sessionHandler;
    private boolean connected = false;

    public SocketHandler() {
        this.host = Config.getProperty("host");
        this.port = Integer.parseInt(Config.getProperty("port"));
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            sessionHandler = ExternalFactory.getSessionHandler(socket);
            connected = true;
            System.out.println("Conectado al servidor: " + host + ":" + port);
            new Thread(() -> sessionHandler.listen()).start();

            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor: " + e.getMessage());
            connected = false;
            return false;
        }
    }

    public void disconnect() {
        try {
            connected = false;
            if (sessionHandler != null) {
                sessionHandler.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("Desconectado del servidor.");
        } catch (IOException e) {
            System.err.println("Error al desconectarse: " + e.getMessage());
        }
    }

    public SessionHandler getSessionHandler() {
        return sessionHandler;
    }

    public boolean isConnected() {
        return connected;
    }
}
