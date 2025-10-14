package com.proyect.server.domain.entity;

import com.proyect.server.domain.User;
import com.proyect.server.domain.interfaces.Content;
import com.proyect.server.domain.interfaces.MeasurableContent;

public class FileContent implements Content, MeasurableContent {
    private static final ContentType type = ContentType.FILE;
    private String name;
    private Long size;
    private User owner;
    private byte[] data;

    public FileContent(String name, User owner, byte[] data) {
        this.name = name;
        this.owner = owner;
        this.data = data;
        setSize();
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setData(byte[] data) {
        this.data = data;
        setSize();
    }
    
    private void setSize(){
        this.size = Long.valueOf(data.length);
    }
    
    @Override
    public ContentType getType() {
       return type;
    }

    @Override
    public Long getSize() {
        return size;
    }
    
    @Override
    public byte[] getData() {
        return data;
    }
}
