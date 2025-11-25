package com.proyectofinal.factory;

import com.proyectofinal.DTO.UserDTO;
import com.proyectofinal.GUI.VentanaPrincipal;
import com.proyectofinal.application.SessionHandler;
import com.proyectofinal.application.SocketHandler;
import com.proyectofinal.controller.CommunicationController;

import java.net.Socket;

public class ExternalFactory {
    public static VentanaPrincipal getVentanaPrincipal() {
        return VentanaPrincipal.getInstance();
    }

    public static SocketHandler getSocketHandler() {
        return new SocketHandler();
    }

    public static CommunicationController getSocketController() {
        return new CommunicationController();
    }

    public static SessionHandler getSessionHandler(Socket socket) {
        return SessionHandler.getInstance(socket);
    }

    public static UserDTO getUserDTO(String username, String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        return userDTO;
    }
}
