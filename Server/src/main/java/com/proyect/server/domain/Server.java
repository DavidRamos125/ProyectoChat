package com.proyect.server.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class Server {

    @Value("${server.port:5000}")
    private int port;

    private ServerSocket serverSocket;
    private final ConcurrentHashMap<String, SessionHandler> sessions = new ConcurrentHashMap<>();
    private Logger logger;

    private volatile boolean running = false;

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            logger.logAccion("Servidor iniciado en el puerto " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();

                String clientKey = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
                logger.logAccion("Nueva conexión desde " + clientKey);

                SessionHandler session = new SessionHandler(clientSocket, this, logger);
                sessions.put(clientKey, session);

                new Thread(String.valueOf(session)).start();
            }

        } catch (IOException e) {
            logger.logAccion("Error al iniciar el servidor: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

            // Cerrar todas las sesiones activas
            for (SessionHandler session : sessions.values()) {
                session.close();
            }

            logger.logAccion("Servidor detenido.");

        } catch (IOException e) {
            logger.logAccion("Error al detener el servidor: " + e.getMessage());
        }
    }

    public void removeSession(String clientKey) {
        sessions.remove(clientKey);
        logger.logAccion("Sesión eliminada: " + clientKey);
    }

    public ConcurrentHashMap<String, SessionHandler> getSessions() {
        return sessions;
    }
}
