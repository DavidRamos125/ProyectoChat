package com.proyect.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class SessionDTO {
    @JsonProperty("id")
    private String id;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("status")
    private String status;

    @JsonProperty("connectionTime")
    private Timestamp connectionTime;

    @JsonProperty("disconnectionTime")
    private Timestamp disconnectionTime;

    // Constructores
    public SessionDTO() {}

    public SessionDTO(String id, int userId, String ip, String status,
                      Timestamp connectionTime, Timestamp disconnectionTime) {
        this.id = id;
        this.userId = userId;
        this.ip = ip;
        this.status = status;
        this.connectionTime = connectionTime;
        this.disconnectionTime = disconnectionTime;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getConnectionTime() { return connectionTime; }
    public void setConnectionTime(Timestamp connectionTime) { this.connectionTime = connectionTime; }

    public Timestamp getDisconnectionTime() { return disconnectionTime; }
    public void setDisconnectionTime(Timestamp disconnectionTime) { this.disconnectionTime = disconnectionTime; }
}
