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
    private final ConcurrentHashMap<String, CommunicationHandler> sessions = new ConcurrentHashMap<>();
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

            for (CommunicationHandler session : sessions.values()) {
                session.logout();
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

    public ConcurrentHashMap<String, CommunicationHandler> getSessions() {
        return sessions;
    }

    public void addSession(String clientKey, CommunicationHandler session) {
        sessions.put(clientKey, session);
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
            logger.logAccion("se ha detenido el servicio de listening: " + e.getMessage(), className);
        }
    }

    public List<CommunicationHandler> getConnectedUsers() {
        return new ArrayList<>(sessions.values());
    }

    public List<CommunicationHandler> getConnectionsByUserId(int userId) {
        return sessions.values().stream()
                .filter(conn -> conn!= null && conn.getCurrentUser().getId() == userId)
                .collect(Collectors.toList());
    }

    public int getCountSessions(int userId) {
        return (int) sessions.values().stream()
                .filter(conn -> conn!= null && conn.getCurrentUser().getId() == userId)
                .count();
    }

}
