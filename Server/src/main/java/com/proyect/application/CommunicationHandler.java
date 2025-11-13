package com.proyect.application;


import com.proyect.util.JSONUtil;

public class CommunicationHandler {
    private Logger logger;
    private ConnectionHandler connectionHandler;

    public CommunicationHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public void handleMessage(String message) {
        String tipo = JSONUtil.getProperty(message, "TIPO_MENSAJE");
        switch (tipo) {
            case "LOGIN":{

            }
            break;

            case "MESSAGE":{
                //TODO
            }
            break;

            case "LOGGOUT":{
                //TODO

            }
            break;

            default:{
                throw new IllegalStateException("Unexpected value: " + tipo);
            }
        }
    }
}
