package com.proyect.application;

import com.proyect.DTO.SessionDTO;
import com.proyect.controller.UserController;
import com.proyect.controller.MessageController;
import com.proyect.DTO.UserDTO;
import com.proyect.DTO.MessageDTO;
import com.proyect.domain.interfaces.IObservableConcrete;
import com.proyect.domain.interfaces.IObserver;
import com.proyect.domain.interfaces.IObserverConcrete;
import com.proyect.factory.ExternalFactory;
import com.proyect.util.Config;
import com.proyect.util.JSONUtil;
import com.proyect.util.JSONBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommunicationHandler implements IObservableConcrete {
    private final Logger logger;
    private ConnectionHandler connectionHandler;
    private UserController userController;
    private MessageController messageController;
    private Server server;
    private UserDTO currentUser;
    private boolean loggedIn;

    private List<IObserverConcrete> observers;
    private SessionDTO currentSession;

    public CommunicationHandler(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
        this.logger = ExternalFactory.getLogger();
        this.userController = ExternalFactory.getUserController();
        this.messageController = ExternalFactory.getMessageController();
        this.server = ExternalFactory.getServer();
        this.observers = new ArrayList<>();
        ExternalFactory.setObserversCommunicationHandler(this);
    }

    public boolean createUserSession(UserDTO user) {
        try {

            this.currentSession = new SessionDTO();
            this.currentSession.setUserId(user.getId());
            this.currentSession.setId(generateSessionId(user.getId()));
            this.currentSession.setIp(connectionHandler.getIp());
            this.currentSession.setStatus("online");
            this.currentSession.setConnectionTime(new Timestamp(System.currentTimeMillis()));
            this.server.addSession(connectionHandler.getClientAddress(), this);

            this.currentUser = user;
            this.loggedIn = true;

            logger.logAccion("Sesión creada para usuario: " + user.getUsername(), this.getClass().getSimpleName());
            return true;

        } catch (Exception e) {
            logger.logAccion("Error creando sesión: " + e.getMessage(), this.getClass().getSimpleName());
            return false;
        }
    }

    private String generateSessionId(int id) {
        return "sess_" + id + UUID.randomUUID();
    }

    public void sendMessage(String message) {
        connectionHandler.send(message);
    }

    public void handleMessage(String jsonMessage) {
        try {
            String action = JSONUtil.getProperty(jsonMessage, "action");
            System.out.println("mensaje recibido:" + jsonMessage);
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
                    logout();
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
            String userJSON = JSONUtil.getProperty(jsonMessage, "user");
            if (userJSON== null) {
                logger.logAccion("Login fallido - datos incompletos", this.getClass().getSimpleName());
                return;
            }

            UserDTO loginDTO = JSONUtil.JSONToObject(userJSON, UserDTO.class);
            loginDTO = userController.login(loginDTO);

            if (loginDTO != null) {
                int conections = server.getCountSessions(loginDTO.getId());
                int maxConections = Integer.parseInt(Config.getProperty("user.maxConnections"));

                if(conections >= maxConections){
                    logger.logAccion("Login fallido por cantidad de sesiones", this.getClass().getSimpleName());
                    return;
                }
                createUserSession(loginDTO);
                List<MessageDTO> userMessages = messageController.getMessagesByUser(loginDTO.getId());

                List<String> connectedUsers = server.getConnectedUsers()
                        .stream()
                        .map(conn -> conn.getCurrentUser())
                        .filter(user -> user != null)
                        .filter(user -> !user.getUsername().equals(currentUser.getUsername()))
                        .map(UserDTO::getUsername)
                        .collect(Collectors.toList());



                String loginResponse = JSONBuilder.create()
                        .add("action", "LOGIN_SUCCESS")
                        .add("user", userJSON)
                        .add("messages", userMessages)
                        .add("connectedUsers", connectedUsers)
                        .add("sessionId", currentSession.getId())
                        .build();

                connectionHandler.send(loginResponse);
                notifyLogin(currentUser);
                logger.logAccion("Login exitoso: " + loginDTO.toString(), this.getClass().getSimpleName());
            } else {
                logger.logAccion("Login fallido - usuario no aceptado: " + loginDTO.toString(), this.getClass().getSimpleName());
            }

        } catch (Exception e) {
            logger.logAccion("Error en login: credenciales invalidas" , this.getClass().getSimpleName());
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
        if (!loggedIn) {
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
            message.setSender(currentUser);
            message.setReceiver(receiver);
            message.setType(messageType != null ? messageType : "TEXT");


            MessageDTO savedMessage = messageController.insertMessage(message);

            if (savedMessage != null) {
                List<CommunicationHandler> receiverConnections = server.getConnectionsByUserId(receiver.getId());


                for (CommunicationHandler receiverConn : receiverConnections) {
                    String messageNotification = JSONBuilder.create()
                            .add("action", "NEW_MESSAGE")
                            .add("message", savedMessage)
                            .add("sender", currentUser.getUsername())
                            .build();

                    receiverConn.sendMessage(messageNotification);
                }

                logger.logAccion("Mensaje enviado de " +
                        currentUser.getUsername() +
                        " a " + receiverUsername, this.getClass().getSimpleName());
            }

        } catch (Exception e) {
            logger.logAccion("Error procesando mensaje: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    public void logout() {
        try {
            notifyLogout(currentUser);
            connectionHandler.close();
            logger.logAccion("Logout: " +
                            (currentUser != null ?
                                    currentUser.getUsername() : "unknown"),
                    this.getClass().getSimpleName());

        } catch (Exception e) {
            logger.logAccion("Error en logout: " + e.getMessage(), this.getClass().getSimpleName());
        }
    }

    public UserDTO getCurrentUser(){
        return currentUser;
    }

    @Override
    public void notify(String data) {
        for (IObserver o : observers) {
            o.update(data);
        }
    }

    @Override
    public void add(IObserver o) {
        observers.add((IObserverConcrete) o);
    }


    @Override
    public void notifyLogin(UserDTO user) {
        for (IObserverConcrete o : observers) {
            o.updateLogin(user);
        }
    }

    @Override
    public void notifyLogout(UserDTO user) {
        for(IObserverConcrete o : observers){
            o.updateLogout(user);
        }
    }
}
