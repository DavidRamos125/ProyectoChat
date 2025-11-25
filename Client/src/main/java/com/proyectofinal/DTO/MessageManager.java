package com.proyectofinal.DTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageManager {
    String userActual;
    private Map<String, List<MessageDTO>> messagesByUser = new HashMap<>();
    private Map<String, UserDTO> availableUsers = new HashMap<>();

    public MessageManager() {
    }

    public MessageManager(Map<String, List<MessageDTO>> messagesByUser,
                    Map<String, UserDTO> availableUsers) {
        this.messagesByUser = messagesByUser;
        this.availableUsers = availableUsers;
    }

    public Map<String, List<MessageDTO>> getMessagesByUser() {
        return messagesByUser;
    }

    public void setMessagesByUser(Map<String, List<MessageDTO>> messagesByUser) {
        this.messagesByUser = messagesByUser;
    }

    public Map<String, UserDTO> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(Map<String, UserDTO> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public void loadAllMessages(List<MessageDTO> allMessages) {
        if (allMessages == null) return;

        // Reiniciamos el mapa para evitar datos duplicados
        messagesByUser.clear();

        for (MessageDTO msg : allMessages) {
            addMessageToUserMap(msg.getSender().getUsername(), msg);
            addMessageToUserMap(msg.getReceiver().getUsername(), msg);
        }

        // Cada lista queda ordenada
        messagesByUser.values().forEach(this::sortListByDate);
    }

    public List<MessageDTO> getMessagesByUser(String username) {
        if (username == null) return Collections.emptyList();

        List<MessageDTO> list = messagesByUser.get(username);
        if (list == null) return Collections.emptyList();

        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    public void newMessage(MessageDTO msg) {
        if (msg == null) return;

        addMessageToUserMap(msg.getSender().getUsername(), msg);
        addMessageToUserMap(msg.getReceiver().getUsername(), msg);

  
        sortListByDate(messagesByUser.get(msg.getSender().getUsername()));
        sortListByDate(messagesByUser.get(msg.getReceiver().getUsername()));
    }


    private void addMessageToUserMap(String username, MessageDTO msg) {
        if (username == null || msg == null) return;

        List<MessageDTO> list = messagesByUser.computeIfAbsent(username, k -> new ArrayList<>());
        list.add(msg);
    }

    private void sortListByDate(List<MessageDTO> list) {
        if (list == null || list.size() <= 1) return;

        list.sort(Comparator.comparing(MessageDTO::getDate));
    }
    
    public void setUserActual(String username){
        this.userActual = username;
    }
}
