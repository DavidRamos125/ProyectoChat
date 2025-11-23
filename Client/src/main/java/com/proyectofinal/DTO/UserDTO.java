package com.proyectofinal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("accepted")
    private boolean accepted;

    public UserDTO() {}

    public UserDTO(int id, String username, String password, boolean accepted) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accepted = accepted;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAccepted() { return accepted; }
    public void setAccepted(boolean accepted) { this.accepted = accepted; }

    @Override
    public String toString() {
        return username;
    }
}
