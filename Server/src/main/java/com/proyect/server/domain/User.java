package com.proyect.server.domain;

import java.util.Date;

public class User {
    private double id;
    private String name;
    private Date registrationDate;
    private int totalMessagesSent;
    private int totalMessagesReceived;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    public void setTotalMessagesSent(int totalMessagesSent) {
        this.totalMessagesSent = totalMessagesSent;
    }

    public int getTotalMessagesReceived() {
        return totalMessagesReceived;
    }

    public void setTotalMessagesReceived(int totalMessagesReceived) {
        this.totalMessagesReceived = totalMessagesReceived;
    }
}
