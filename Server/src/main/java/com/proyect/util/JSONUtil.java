package com.proyect.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {

    private static final ObjectMapper mapper = new ObjectMapper()
            .disable(SerializationFeature.INDENT_OUTPUT)
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String getProperty(String jsonString, String property) {
    try {
        JsonNode node = mapper.readTree(jsonString).path(property);
        if (node.isMissingNode()) {
            return null;

        }
        if (node.isValueNode()) {
            return node.asText();
        }
        return mapper.writeValueAsString(node);
    } catch (Exception e) {
        throw new RuntimeException(
                "Error al obtener propiedad '" + property + "' del JSON", e);
    }
}



    public static String objectToJSON(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir objeto a JSON", e);
        }
    }

    public static <T> T JSONToObject(String json, Class<T> baseClass) {
        try {
            return mapper.readValue(json, baseClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a objeto", e);
        }
    }
}
