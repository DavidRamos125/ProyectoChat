package com.proyect.controller;

import com.proyect.application.ConnectionHandler;
import com.proyect.application.Server;
import com.proyect.factory.ExternalFactory;

import java.util.concurrent.ConcurrentHashMap;

public class ServerController {
    private final Server server;
    
    public ServerController() {
        server = ExternalFactory.getServer();
    }

    public void start() {
        new Thread(server).start();
    }

    public ConcurrentHashMap<String, ConnectionHandler> getSessions() {
        return server.getSessions();
    }

    public void stop() {
        server.stop();
    }

    public void removeSession(String clientKey) {
        server.removeSession(clientKey);
    }
}
