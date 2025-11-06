package com.proyect.server.domain;

import com.proyect.server.domain.interfaces.Content;

public class TextContent implements Content {
    private static final ContentType type = ContentType.TEXT;
    private String text;
    
    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }
    @Override
    public ContentType getType() {
        return type;
    }
    
}
    