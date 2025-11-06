package com.proyect.server.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class EMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private EUser sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private EUser receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ContentType contentType;

    @Column(columnDefinition = "TEXT")
    private String contentText;

    @ManyToOne
    @JoinColumn(name = "content_file_id")
    private EFileContent contentFile;

    @Column(nullable = false)
    private LocalDateTime dateSent = LocalDateTime.now();

    public enum ContentType {
        TEXT, FILE
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public EUser getSender() { return sender; }
    public void setSender(EUser sender) { this.sender = sender; }

    public EUser getReceiver() { return receiver; }
    public void setReceiver(EUser receiver) { this.receiver = receiver; }

    public ContentType getContentType() { return contentType; }
    public void setContentType(ContentType contentType) { this.contentType = contentType; }

    public String getContentText() { return contentText; }
    public void setContentText(String contentText) { this.contentText = contentText; }

    public EFileContent getContentFile() { return contentFile; }
    public void setContentFile(EFileContent contentFile) { this.contentFile = contentFile; }

    public LocalDateTime getDateSent() { return dateSent; }
    public void setDateSent(LocalDateTime dateSent) { this.dateSent = dateSent; }
}

