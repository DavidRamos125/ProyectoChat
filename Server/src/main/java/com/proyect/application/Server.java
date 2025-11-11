package com.proyect.application;

import com.proyect.domain.Session;
import com.proyect.domain.User;
import com.proyect.factory.ExternalFactory;
import com.proyect.util.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class Server implements Runnable {
    private final int port;
    private ServerSocket serverSocket;
    private final ConcurrentHashMap<String, SessionHandler> sessions = new ConcurrentHashMap<>();
    private final Logger logger;
    private static Server instance;
    private volatile boolean running = false;

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
        this.running = false;
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

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

    public Logger getLogger() {
        return logger;
    }

    public void acceptClient(SessionHandler sessionHandler, String clientKey) {
//        Session session = sessionHandler.getSession();
//        User user = session.getUser();
//        if(verifyMaxSessions(user)) {
//            sessions.put(clientKey, sessionHandler);
//            new Thread(sessionHandler).start();
//        }
        sessions.put(clientKey, sessionHandler);
        new Thread(sessionHandler).start();
    }

    public boolean verifyMaxSessions(User user) {
        long count = sessions.values().stream()
                .filter(s -> s.getSession().getUser().equals(user))
                .count();

        return count < Integer.parseInt(Config.getProperty("max.sessions"));
    }


    @Override
    public void run() {
        running = true;
        logger.logAccion("Servidor iniciado en el puerto " + port);
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
                logger.logAccion("Nueva conexión desde " + clientKey);

                SessionHandler session = new SessionHandler(clientSocket);
                acceptClient(session, clientKey);
            }
        } catch (IOException e) {
            logger.logAccion("se ha detenido el servidor: " + e.getMessage());
        }
    }
}
