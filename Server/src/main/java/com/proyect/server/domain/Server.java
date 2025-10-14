package com.proyect.server.domain;

import org.springframework.beans.factory.annotation.Value;

import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    @Value("${server.port}")
    private int port;
    private ServerSocket serverSocket;
    private ConcurrentHashMap<String, ClientHandler> connections;
    private Logger logger;
}
