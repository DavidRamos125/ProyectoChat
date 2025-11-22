package com.proyect.application;

import com.proyect.controller.ServerController;
import com.proyect.domain.Session;
import com.proyect.factory.ExternalFactory;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final ServerController server;
    private final Logger logger;
    private final String className;
    private BufferedReader in;
    private PrintWriter out;
    private String clientKey;
    private Session session;
    private boolean loggedIn;
    private ConcurrentHashMap<String, ConnectionHandler> sessions;
    private CommunicationHandler communicationHandler;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        this.server = ExternalFactory.getServerController();
        this.logger = ExternalFactory.getLogger();
        this.className = this.getClass().getSimpleName();
        this.sessions = server.getSessions();
        this.loggedIn = false;
        this.communicationHandler = ExternalFactory.getCommunicationHandler(this);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            logger.logAccion(" Conexion establecida - ID: " + clientKey, className);
            out.println("Conexión establecida con el servidor.");

            String message;
            while ((message = in.readLine()) != null) {
                communicationHandler.handleMessage(message);
            }

        } catch (IOException e) {
            logger.logAccion("Error en sesión " + clientKey + ": " + e.getMessage(), className);
        } finally {
            close();
        }
    }

    public Session getSession() {
        return session;
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            server.removeSession(clientKey);
            logger.logAccion("Sesión cerrada: " + clientKey, className);
        } catch (IOException e) {
            logger.logAccion("Error al cerrar sesión " + clientKey + ": " + e.getMessage(), className);
        }
    }
}
