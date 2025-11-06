package com.proyect.server.infrastructure.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class EUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private Boolean accepted = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<EFileContent> files;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ESession> sessions;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<EMessage> messagesSent;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<EMessage> messagesReceived;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
}