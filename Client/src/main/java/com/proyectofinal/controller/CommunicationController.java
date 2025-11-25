package com.proyectofinal.controller;

import com.proyectofinal.application.SessionHandler;
import com.proyectofinal.application.SocketHandler;
import com.proyectofinal.factory.ExternalFactory;

public class CommunicationController {
    private SocketHandler socket;
    private SessionHandler session;

    public CommunicationController() {
        this.socket = ExternalFactory.getSocketHandler();
    }

    public void sendMessage(String message) {
        this.session.send(message);
    }

    public void sendLogin(String username, String password) {
        this.session.sendLogin(username, password);
    }

    public void sendRegister(String username, String password) {
        this.session.sendRegister(username, password);
    }

    public void connect() {
        this.socket.connect();
        this.session = socket.getSessionHandler();
    }

    public boolean isConnected() {
        return this.socket.isConnected();
    }

    public void disconnect() {
        if(session.isConnected()) {
            session.close();
        }
        this.socket.disconnect();
    }
}
