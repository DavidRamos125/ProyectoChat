package com.proyect.application;

import com.proyect.controller.ServerController;
import com.proyect.domain.Session;
import com.proyect.domain.User;
import com.proyect.factory.ExternalFactory;

import java.io.*;
import java.net.Socket;

public class SessionHandler implements Runnable {

    private final Socket socket;
    private final ServerController server;
    private final Logger logger;
    private BufferedReader in;
    private PrintWriter out;
    private final String clientKey;
    private Session session;

    public SessionHandler(Socket socket) {
        this.socket = socket;
        this.server = ExternalFactory.getServerController();
        this.logger = ExternalFactory.getLogger();
        this.clientKey = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            logger.logAccion("Sesión iniciada: " + clientKey);
            out.println("Conexión establecida con el servidor.");

            String message;
            while ((message = in.readLine()) != null) {
                logger.logAccion("Mensaje recibido de " + clientKey + ": " + message);
                out.println("Servidor recibió: " + message);
            }

        } catch (IOException e) {
            logger.logAccion("Error en sesión " + clientKey + ": " + e.getMessage());
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
            logger.logAccion("Sesión cerrada: " + clientKey);
        } catch (IOException e) {
            logger.logAccion("Error al cerrar sesión " + clientKey + ": " + e.getMessage());
        }
    }
}
