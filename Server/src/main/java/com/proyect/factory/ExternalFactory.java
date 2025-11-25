package com.proyect.factory;

import com.proyect.DTO.UserDTO;
import com.proyect.GUI.VentanaPrincipal;
import com.proyect.application.CommunicationHandler;
import com.proyect.application.ConnectionHandler;
import com.proyect.application.Logger;
import com.proyect.application.Server;
import com.proyect.controller.MessageController;
import com.proyect.controller.ServerController;
import com.proyect.controller.UserController;
import com.proyect.domain.interfaces.IObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ExternalFactory {

    public static CommunicationHandler getCommunicationHandler(ConnectionHandler connectionHandler) {
        return new CommunicationHandler(connectionHandler);
    }

    public static ServerController getServerController() {
        return new ServerController();
    }

    public static Server getServer() {
        return Server.getInstance();
    }

    public static Logger getLogger() {
        return Logger.getInstance();
    }

    public static UserDTO getUserDTO() {
        return new UserDTO();
    }

    public static ServerSocket getSocketServer(int port){
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static VentanaPrincipal getVentanaPrincipal() {
        return VentanaPrincipal.getInstance();
    }

    public static void setObserversCommunicationHandler(CommunicationHandler communicationHandler) {
        communicationHandler.add(getVentanaPrincipal());
    }

    public static UserController getUserController() {
        return new UserController();
    }

    public static MessageController getMessageController() {
        return new MessageController();
    }
}
