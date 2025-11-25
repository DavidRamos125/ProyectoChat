package com.proyect.controller;

import com.proyect.application.CommunicationHandler;
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

    public ConcurrentHashMap<String, CommunicationHandler> getSessions() {
        return server.getSessions();
    }

    public void stop() {
        server.stop();
    }

}
