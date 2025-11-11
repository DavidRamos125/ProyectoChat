package com.proyect.factory;

import com.proyect.GUI.VentanaPrincipal;
import com.proyect.application.Logger;
import com.proyect.application.Server;
import com.proyect.controller.ServerController;

import java.io.IOException;
import java.net.ServerSocket;

public class ExternalFactory {

    public static ServerController getServerController() {
        return new ServerController();
    }

    public static Server getServer() {
        return Server.getInstance();
    }

    public static Logger getLogger() {
        return Logger.getInstance();
    }

    public static ServerSocket getSocketServer(int port){
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VentanaPrincipal getVentanaPrincipal() {
        return new VentanaPrincipal();
    }
}
