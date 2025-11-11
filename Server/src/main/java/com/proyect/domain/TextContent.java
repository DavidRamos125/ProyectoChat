package com.proyect.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.proyect.domain.interfaces.Content;

@JsonTypeName("TEXT")
public class TextContent implements Content {
    private static final ContentType type = ContentType.TEXT;
    private String text;

    public TextContent() {}
    
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
    