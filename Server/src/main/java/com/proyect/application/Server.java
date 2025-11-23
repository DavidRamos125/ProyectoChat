package com.proyect.application;

import com.proyect.factory.ExternalFactory;
import com.proyect.util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class Server implements Runnable {
    private final int port;
    private ServerSocket serverSocket;
    private final ConcurrentHashMap<String, ConnectionHandler> sessions = new ConcurrentHashMap<>();
    private final Logger logger;
    private final String className;
    private volatile boolean running = false;

    private static Server instance;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private Server() {
        this.port = Integer.parseInt(Config.getProperty("server.port"));
        this.serverSocket = ExternalFactory.getSocketServer(port);
        this.logger = ExternalFactory.getLogger();
        this.className = this.getClass().getSimpleName();
        this.running = false;
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            for (ConnectionHandler session : sessions.values()) {
                session.close();
            }

            logger.logAccion("Servidor detenido.", className);

        } catch (IOException e) {
            logger.logAccion("Error al detener el servidor: " + e.getMessage(), className);
        }
    }

    public void removeSession(String clientKey) {
        sessions.remove(clientKey);
        logger.logAccion("Sesión eliminada: " + clientKey, className);
    }

    public ConcurrentHashMap<String, ConnectionHandler> getSessions() {
        return sessions;
    }

    public Logger getLogger() {
        return logger;
    }


    @Override
    public void run() {
        running = true;
        logger.logAccion("Servidor iniciado en el puerto " + port, className);
        if (serverSocket.isClosed()) {
            this.serverSocket = ExternalFactory.getSocketServer(port);
        }
        listening();
    }

    private void listening() {
        try {

            while (running) {
                Socket clientSocket = serverSocket.accept();
                String clientKey = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                logger.logAccion("Nueva conexión desde " + clientKey, className);

                ConnectionHandler session = new ConnectionHandler(clientSocket);
                new Thread(session).start();
            }
        } catch (IOException e) {
            logger.logAccion("se ha detenido el servidor: " + e.getMessage(), className);
        }
    }

    public List<ConnectionHandler> getConnectedUsers() {
        return new ArrayList<>(sessions.values());
    }

    public List<ConnectionHandler> getConnectionsByUserId(int userId) {
        return sessions.values().stream()
                .filter(conn -> conn.getCurrentUser() != null && conn.getCurrentUser().getId() == userId)
                .collect(Collectors.toList());
    }

}
