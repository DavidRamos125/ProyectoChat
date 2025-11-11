package com.proyect.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.proyect.domain.interfaces.Content;
import com.proyect.domain.interfaces.MeasurableContent;

import java.util.UUID;

@JsonTypeName("FILE")
public class FileContent implements Content, MeasurableContent {
    private Long size;
    private final String id;
    private static final ContentType type = ContentType.FILE;
    private String name;
    private byte[] data;

    public FileContent() {
        this.id = UUID.randomUUID().toString();
    }

    public FileContent(String name, String id, byte[] data) {
        this.name = name;
        this.id = id;
        this.data = data;
        setSize();
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
