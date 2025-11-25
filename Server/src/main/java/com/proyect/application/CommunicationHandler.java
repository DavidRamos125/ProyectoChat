package com.proyect.application;

import com.proyect.controller.UserController;
import com.proyect.controller.MessageController;
import com.proyect.DTO.UserDTO;
import com.proyect.DTO.MessageDTO;
import com.proyect.domain.interfaces.IObservable;
import com.proyect.domain.interfaces.IObserver;
import com.proyect.factory.ExternalFactory;
import com.proyect.util.JSONUtil;
import com.proyect.util.JSONBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommunicationHandler implements IObservable{
    private Logger logger;
    private ConnectionHandler connectionHandler;
    private UserController userController;
    private MessageController messageController;
    private Server server;
    
    private List<IObserver> observers;

    public CommunicationHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.logger = ExternalFactory.getLogger();
        this.userController = ExternalFactory.getUserController();
        this.messageController = ExternalFactory.getMessageController();
        this.server = ExternalFactory.getServer();
        this.observers = new ArrayList<>();
        ExternalFactory.setObserversCommunicationHandler(this);
    }

    public void handleMessage(String jsonMessage) {
        try {
            String action = JSONUtil.getProperty(jsonMessage, "action");
            System.out.println("mensaje recibido:"+jsonMessage);
            switch (action) {
                case "LOGIN":
                    handleLogin(jsonMessage);
                    break;

                case "REGISTER":
                    handleRegister(jsonMessage);
                    break;

                case "MESSAGE":
                    processMessage(jsonMessage);
                    break;

                case "LOGOUT":
                    handleLogout(jsonMessage);
                    break;

                default:
                    logger.logAccion("Acción desconocida: " + action, this.getClass().getSimpleName());
                    break;
            }
        } catch (Exception e) {
            logger.logAccion("Error procesando mensaje: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    private void handleLogin(String jsonMessage) {
        try {
            String user = JSONUtil.getProperty(jsonMessage, "user");
            System.out.println(">>"+user+"<<");
            if (user == null) {
                logger.logAccion("Login fallido - datos incompletos", this.getClass().getSimpleName());
                return;
            }

            UserDTO loginDTO = JSONUtil.JSONToObject(user, UserDTO.class);
            System.out.println(">>"+loginDTO+"<<");
            loginDTO = userController.login(loginDTO);

            if (loginDTO != null) {
                connectionHandler.createUserSession(loginDTO);
                List<MessageDTO> userMessages = messageController.getMessagesByUser(loginDTO.getId());

                List<String> connectedUsers = server.getConnectedUsers()
                        .stream()
                        .map(conn -> {
                            UserDTO connectedUser = conn.getCurrentUser();
                            return connectedUser != null ? connectedUser.getUsername() : "unknown";
                        })
                        .distinct()
                        .collect(Collectors.toList());

                String loginResponse = JSONBuilder.create()
                        .add("action", "LOGIN_SUCCESS")
                        .add("user", user)
                        .add("messages", userMessages)
                        .add("connectedUsers", connectedUsers)
                        .add("sessionId", connectionHandler.getCurrentSession().getId())
                        .build();

                connectionHandler.sendMessage(loginResponse);
                logger.logAccion("Login exitoso: " + loginDTO.toString(), this.getClass().getSimpleName());
            } else {
                logger.logAccion("Login fallido - usuario no aceptado: " + loginDTO.toString(), this.getClass().getSimpleName());
            }

        } catch (Exception e) {
            logger.logAccion("Error en login: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    private void handleRegister(String jsonMessage) {
        try {
            UserDTO newUser;
            String userJson = JSONUtil.getProperty(jsonMessage, "user");
            newUser = JSONUtil.JSONToObject(userJson, UserDTO.class);
            UserDTO createdUser = userController.createUser(newUser);

            if (createdUser != null) {
                logger.logAccion("Usuario registrado: " + createdUser.toString(), this.getClass().getSimpleName());
            }
            notify("NUEVO_PENDIENTE");
        } catch (Exception e) {
            logger.logAccion("Error registrando usuario: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }
    
    private void processMessage(String jsonMessage) {
        if (!connectionHandler.isLoggedIn()) {
            logger.logAccion("Intento de enviar mensaje sin login", this.getClass().getSimpleName());
            return;
        }

        try {
            String receiverUsername = JSONUtil.getProperty(jsonMessage, "receiver");
            String content = JSONUtil.getProperty(jsonMessage, "content");
            String messageType = JSONUtil.getProperty(jsonMessage, "type");

            if (receiverUsername == null || content == null) {
                logger.logAccion("Mensaje incompleto", this.getClass().getSimpleName());
                return;
            }

            String idReceiver = JSONUtil.getProperty(receiverUsername, "id");

            UserDTO receiver = userController.getUserById(Integer.parseInt(idReceiver));
            if (receiver == null) {
                logger.logAccion("Usuario receptor no encontrado: " + receiverUsername, this.getClass().getSimpleName());
                return;
            }

            MessageDTO message = new MessageDTO();
            message.setSender(connectionHandler.getCurrentUser());
            message.setReceiver(receiver);
            message.setType(messageType != null ? messageType : "TEXT");


            if ("TEXT".equals(messageType)) {
                // Para mensajes de texto, necesitamos crear el TextContent
                // Esto depende de cómo esté estructurado tu MessageDTO
            }

            // Guardar en base de datos
            MessageDTO savedMessage = messageController.insertMessage(message);

            if (savedMessage != null) {
                List<ConnectionHandler> receiverConnections = server.getConnectionsByUserId(receiver.getId());


                for (ConnectionHandler receiverConn : receiverConnections) {
                    String messageNotification = JSONBuilder.create()
                            .add("action", "NEW_MESSAGE")
                            .add("message", savedMessage)
                            .add("sender", connectionHandler.getCurrentUser().getUsername())
                            .build();

                    receiverConn.sendMessage(messageNotification);
                }

                logger.logAccion("Mensaje enviado de " +
                        connectionHandler.getCurrentUser().getUsername() +
                        " a " + receiverUsername, this.getClass().getSimpleName());
            }

        } catch (Exception e) {
            logger.logAccion("Error procesando mensaje: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    private void handleLogout(String jsonMessage) {
        try {
            connectionHandler.logout();
            logger.logAccion("Logout: " +
                            (connectionHandler.getCurrentUser() != null ?
                                    connectionHandler.getCurrentUser().getUsername() : "unknown"),
                    this.getClass().getSimpleName());

        } catch (Exception e) {
            logger.logAccion("Error en logout: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    @Override
    public void notify(String data) {
        for(IObserver o :observers){
            o.update(data);
        }
    }

    @Override
    public void add(IObserver o) {
        observers.add(o);
    }

}
