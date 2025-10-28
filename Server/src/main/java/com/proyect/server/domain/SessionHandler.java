package com.proyect.server.domain;

import java.io.*;
import java.net.Socket;

public class SessionHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private final Logger logger;
    private BufferedReader in;
    private PrintWriter out;
    private String clientKey;

    public SessionHandler(Socket socket, Server server, Logger logger) {
        this.socket = socket;
        this.server = server;
        this.logger = logger;
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
                System.out.println("📩 Mensaje recibido de " + clientKey + ": " + message);
                logger.logAccion("Mensaje recibido de " + clientKey + ": " + message);


                out.println("Servidor recibió: " + message);

            }

        } catch (IOException e) {
            logger.logAccion("Error en sesión " + clientKey + ": " + e.getMessage());
        } finally {
            close();
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            server.removeSession(clientKey);
            logger.logAccion("Sesión cerrada: " + clientKey);
            System.out.println("❌ Cliente desconectado: " + clientKey);
        } catch (IOException e) {
            logger.logAccion("Error al cerrar sesión " + clientKey + ": " + e.getMessage());
        }
    }
}
