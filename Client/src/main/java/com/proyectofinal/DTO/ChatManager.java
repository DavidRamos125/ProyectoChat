package com.proyectofinal.DTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatManager {
    private String userActual;
    private Map<String, List<MessageDTO>> messagesByUser = new HashMap<>();
    private Map<String, String> userStatus = new HashMap<>(); 
    private static ChatManager instance;

    public ChatManager(String userActual) {
        this.userActual = userActual;
    }

    public Map<String, List<MessageDTO>> getMessagesByUser() {
        return messagesByUser;
    }

    public void setMessagesByUser(Map<String, List<MessageDTO>> messagesByUser) {
        this.messagesByUser = messagesByUser;
    }

    public Map<String, String> getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Map<String, String> userStatus) {
        this.userStatus = userStatus;
    }

    public void loadAvailableUsers(List<UserDTO> availableUsersList) {
        if (availableUsersList == null) return;
        
        for (UserDTO user : availableUsersList) {
            String username = user.getUsername();
            // Solo añadimos si no es el usuario actual
            if (!username.equals(userActual)) {
                userStatus.put(username, "available");
            }
        }
    }

    public void loadAllMessages(List<MessageDTO> allMessages) {
        if (allMessages == null) return;
        messagesByUser.clear();

        for (MessageDTO msg : allMessages) {
            String otherUser = getOtherUser(msg);
            if (otherUser != null && !otherUser.equals(userActual)) {
                addMessageToUserMap(otherUser, msg);
                addUserFromMessage(otherUser);
            }
        }

        messagesByUser.values().forEach(this::sortListByDate);
    }

    public List<MessageDTO> getMessagesByUser(String username) {
        if (username == null || username.equals(userActual)) return Collections.emptyList();

        List<MessageDTO> list = messagesByUser.get(username);
        if (list == null) return Collections.emptyList();

        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    public void newMessage(MessageDTO msg) {
        if (msg == null) return;

        String otherUser = getOtherUser(msg);
        if (otherUser == null || otherUser.equals(userActual)) return;

        addMessageToUserMap(otherUser, msg);
        addUserFromMessage(otherUser);
        sortListByDate(messagesByUser.get(otherUser));
    }

    public void userConnected(UserDTO user) {
        if (user != null && user.getUsername() != null) {
            String username = user.getUsername();
            // Solo añadimos si no es el usuario actual
            if (!username.equals(userActual)) {
                userStatus.put(username, "available");
            }
        }
    }

    public void userDisconnected(String username) {
        if (username != null && !username.equals(userActual)) {
            userStatus.put(username, "unavailable");
        }
    }

    public boolean isUserAvailable(String username) {
        if (username == null || username.equals(userActual)) return false;
        return "available".equals(userStatus.get(username));
    }

    public String getUserStatus(String username) {
        if (username == null || username.equals(userActual)) return "unknown";
        return userStatus.getOrDefault(username, "unavailable");
    }

    private void addMessageToUserMap(String username, MessageDTO msg) {
        if (username == null || msg == null || username.equals(userActual)) return;

        List<MessageDTO> list = messagesByUser.computeIfAbsent(username, k -> new ArrayList<>());
        
        // Verificamos si el mensaje ya existe para evitar duplicados
        if (!containsMessage(list, msg)) {
            list.add(msg);
        }
    }
    
    private String getOtherUser(MessageDTO msg) {
        if (msg == null || msg.getSender() == null || msg.getReceiver() == null) 
            return null;
            
        String sender = msg.getSender().getUsername();
        String receiver = msg.getReceiver().getUsername();
        
        if (userActual.equals(sender)) {
            return receiver;
        } else if (userActual.equals(receiver)) {
            return sender;
        }
        return null;
    }

    private void addUserFromMessage(String username) {
        if (username != null && !username.equals(userActual)) {
            userStatus.putIfAbsent(username, "unavailable");
        }
    }

    private boolean containsMessage(List<MessageDTO> list, MessageDTO msg) {
        if (list == null || msg == null) return false;
        
        return list.stream().anyMatch(existingMsg -> 
            existingMsg.getId() == msg.getId() || 
            (existingMsg.getDate().equals(msg.getDate()) && 
             existingMsg.getSender().getUsername().equals(msg.getSender().getUsername()) && 
             existingMsg.getReceiver().getUsername().equals(msg.getReceiver().getUsername()))
        );
    }

    private void sortListByDate(List<MessageDTO> list) {
        if (list == null || list.size() <= 1) return;

        list.sort(Comparator.comparing(MessageDTO::getDate));
    }
    
    public void setUserActual(String username){
        this.userActual = username;
    }
    
    public String getUserActual() {
        return userActual;
    }
}
