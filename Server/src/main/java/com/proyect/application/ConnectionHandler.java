package com.proyect.application;

import com.proyect.controller.UserController;
import com.proyect.factory.ExternalFactory;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final Server server;
    private final Logger logger;
    private BufferedReader in;
    private PrintWriter out;
    private String clientAddress;
    private CommunicationHandler communicationHandler;
    private UserController userController;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        this.server = ExternalFactory.getServer();
        this.logger = ExternalFactory.getLogger();
        this.clientAddress = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.userController = ExternalFactory.getUserController();
        this.communicationHandler = new CommunicationHandler(this);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            logger.logAccion("Conexión establecida desde: " + clientAddress, this.getClass().getSimpleName());

            String message;
            while ((message = in.readLine()) != null && !socket.isClosed()) {
                communicationHandler.handleMessage(message);
            }

        } catch (IOException e) {
            logger.logAccion("Error en conexión " + clientAddress + ": " + e.getMessage(), this.getClass().getSimpleName());
        } finally {
            close();
        }
    }

    public synchronized void send(String message) {
        if (out != null && !socket.isClosed()) {
            out.println(message);
        }
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            server.removeSession(clientAddress);
            logger.logAccion("Conexión cerrada: " + clientAddress, this.getClass().getSimpleName());
        } catch (IOException e) {
            logger.logAccion("Error al cerrar conexión: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    public String getIp(){
        return socket.getInetAddress().getHostAddress();
    }

    public String getClientAddress(){
        return clientAddress;
    }
    
    public CommunicationHandler getCommunicationHandler(){
        return communicationHandler;
    }

}
