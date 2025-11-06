package com.proyect.server.infrastructure.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class EFileContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private EUser owner;

    @Column(nullable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public EUser getOwner() { return owner; }
    public void setOwner(EUser owner) { this.owner = owner; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
