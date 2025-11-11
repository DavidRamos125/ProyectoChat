package com.proyectofinal.controller;

import com.proyectofinal.application.SocketHandler;
import com.proyectofinal.factory.ExternalFactory;

public class SocketController {
    private SocketHandler socket;

    public SocketController() {
        this.socket = ExternalFactory.getSocketHandler();
    }

    public void sendMessage(String message) {
        this.socket.sendMessage(message);
    }

    public void connect() {
        this.socket.connect();
    }

    public void disconnect() {
        this.socket.disconnect();
    }
}
