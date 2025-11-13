package com.proyect.domain;

import java.util.Date;

public class Session {
    private String id;
    private User user;
    private String ip;
    private Date ConnectionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getConnectionTime() {
        return ConnectionTime;
    }

    public void setConnectionTime(Date ConnectionTime) {
        this.ConnectionTime = ConnectionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
