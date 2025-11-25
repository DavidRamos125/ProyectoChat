package com.proyectofinal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class MessageDTO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("sender")
    private UserDTO sender;

    @JsonProperty("receiver")
    private UserDTO receiver;

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

    public MessageDTO(int id, UserDTO sender, UserDTO receiver,
                      String type, String textContent,
                      String fileName, Long fileSize, byte[] fileData, Timestamp date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
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
