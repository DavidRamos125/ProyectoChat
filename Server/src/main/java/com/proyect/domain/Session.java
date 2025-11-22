package com.proyect.domain;

import java.sql.Timestamp;
import java.util.UUID;

public class Session {
    private String id;
    private User user;
    private String ip;
    private String status;
    private Timestamp connectionTime;
    private Timestamp disconnectionTime;

    public Session() {
        this.id = "session-" + UUID.randomUUID();
    }

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

    public Timestamp getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(Timestamp ConnectionTime) {
        this.connectionTime = ConnectionTime;
    }

    public Timestamp getDisconnectionTime() {
        return disconnectionTime;
    }

    public void setDisconnectionTime(Timestamp DisconnectionTime) {
        this.disconnectionTime = DisconnectionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", ip='" + ip + '\'' +
                ", connectionTime=" + connectionTime +
                ", disconnectionTime=" + disconnectionTime +
                '}';
    }
}
