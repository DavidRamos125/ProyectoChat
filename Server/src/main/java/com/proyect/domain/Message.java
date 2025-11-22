package com.proyect.domain;

import com.proyect.domain.interfaces.Content;

import java.sql.Timestamp;
import java.util.List;

public class Message {
    private int id;
    private User sender;
    private Session sessionSender;
    private User receiver;
    private List<Session> sessionsReceived;
    private Content content;
    private Timestamp date;

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public Session getSessionSender() {
        return sessionSender;
    }

    public void setSessionSender(Session sessionSender) {
        this.sessionSender = sessionSender;
    }

    public List<Session> getSessionsReceived() {
        return sessionsReceived;
    }

    public void setSessionsReceived(List<Session> sessionsReceived) {
        this.sessionsReceived = sessionsReceived;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
