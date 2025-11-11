package com.proyectofinal.application;

import java.io.*;
import java.net.Socket;

public class MessageHandler {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean listening = true;

    public MessageHandler(Socket socket) {
        try {
            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error al inicializar MessageHandler: " + e.getMessage());
        }
    }

    public void send(String message) {
        if (output != null) {
            output.println(message);
            System.out.println("Mensaje enviado: " + message);
        }
    }

    public void listen() {
        try {
            String line;
            while (listening && (line = input.readLine()) != null) {
                processMessage(line);
            }
        } catch (IOException e) {
            System.err.println("Error al recibir mensaje: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void processMessage(String message) {
        System.out.println("Mensaje recibido: " + message);

        // Ejemplo: comandos simples
        if (message.equalsIgnoreCase("PING")) {
            send("PONG");
        }
    }

    public void close() {
        listening = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar MessageHandler: " + e.getMessage());
        }
    }
}
