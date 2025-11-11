package com.proyect.domain.interfaces;

import com.proyect.domain.ContentType;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.proyect.domain.FileContent;
import com.proyect.domain.TextContent;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextContent.class, name = "TEXT"),
        @JsonSubTypes.Type(value = FileContent.class, name = "FILE")
})

public interface Content {
    ContentType getType();
}
