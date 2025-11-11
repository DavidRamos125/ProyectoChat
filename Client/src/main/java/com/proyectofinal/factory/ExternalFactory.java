package com.proyectofinal.factory;

import com.proyectofinal.application.SocketHandler;
import com.proyectofinal.controller.SocketController;

public class ExternalFactory {
    public static SocketHandler getSocketHandler() {
        return new SocketHandler();
    }

    public static SocketController getSocketController() {
        return new SocketController();
    }
}
