package com.proyect.server.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class ESession {

    @Id
    @Column(length = 64)
    private String id; // UUID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private EUser user;

    @Column(nullable = false, length = 45)
    private String ip;

    private LocalDateTime disconnectionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.CONNECTED;

    public enum Status {
        CONNECTED, DISCONNECTED
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public EUser getUser() { return user; }
    public void setUser(EUser user) { this.user = user; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public LocalDateTime getDisconnectionTime() { return disconnectionTime; }
    public void setDisconnectionTime(LocalDateTime disconnectionTime) { this.disconnectionTime = disconnectionTime; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
