package com.proyect.server.domain.entity;

import com.proyect.server.domain.interfaces.Content;
import com.proyect.server.domain.interfaces.MeasurableContent;

public class FileContent implements Content, MeasurableContent {
    private Long size;
    private String id;
    private static final ContentType type = ContentType.FILE;
    private String name;
    private byte[] data;

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
