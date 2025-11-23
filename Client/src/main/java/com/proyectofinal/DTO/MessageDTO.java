package com.proyectofinal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.util.List;

public class MessageDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("sender")
    private UserDTO sender;

    @JsonProperty("receiver")
    private UserDTO receiver;

    @JsonProperty("sessionSender")
    private SessionDTO sessionSender;

    @JsonProperty("sessionsReceived")
    private List<SessionDTO> sessionsReceived;

    @JsonProperty("type")
    private String type; // "TEXT" o "FILE"

    @JsonProperty("textContent")
    private String textContent;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("fileSize")
    private Long fileSize;

    @JsonProperty("fileData")
    private byte[] fileData;

    @JsonProperty("date")
    private Timestamp date;

    // Constructores
    public MessageDTO() {}

    public MessageDTO(int id, UserDTO sender, UserDTO receiver, SessionDTO sessionSender,
                      List<SessionDTO> sessionsReceived, String type, String textContent,
                      String fileName, Long fileSize, byte[] fileData, Timestamp date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.sessionSender = sessionSender;
        this.sessionsReceived = sessionsReceived;
        this.type = type;
        this.textContent = textContent;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileData = fileData;
        this.date = date;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public UserDTO getSender() { return sender; }
    public void setSender(UserDTO sender) { this.sender = sender; }

    public UserDTO getReceiver() { return receiver; }
    public void setReceiver(UserDTO receiver) { this.receiver = receiver; }

    public SessionDTO getSessionSender() { return sessionSender; }
    public void setSessionSender(SessionDTO sessionSender) { this.sessionSender = sessionSender; }

    public List<SessionDTO> getSessionsReceived() { return sessionsReceived; }
    public void setSessionsReceived(List<SessionDTO> sessionsReceived) { this.sessionsReceived = sessionsReceived; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTextContent() { return textContent; }
    public void setTextContent(String textContent) { this.textContent = textContent; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
}
