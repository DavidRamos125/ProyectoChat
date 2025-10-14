package com.proyect.server.domain;
import java.util.Date;

public class Session {
    private String id;
    private User user;
    private String ip;
    private Date ConnectinoTime;
    private String status;

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

    public Date getConnectinoTime() {
        return ConnectinoTime;
    }

    public void setConnectinoTime(Date ConnectinoTime) {
        this.ConnectinoTime = ConnectinoTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
