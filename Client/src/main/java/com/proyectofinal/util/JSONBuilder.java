package com.proyectofinal.util;

import java.util.HashMap;
import java.util.Map;


public class JSONBuilder {
    private Map<String, Object> jsonMap;

    public JSONBuilder() {
        this.jsonMap = new HashMap<>();
    }

    public JSONBuilder add(String key, Object value) {
        jsonMap.put(key, value);
        return this;
    }

    public String build() {
        return JSONUtil.objectToJSON(jsonMap);
    }

    public static JSONBuilder create() {
        return new JSONBuilder();
    }


    public static String error(String message) {
        return create()
                .add("status", "error")
                .add("message", message)
                .add("timestamp", System.currentTimeMillis())
                .build();
    }

    public static String data(String key, Object data) {
        return create()
                .add("status", "success")
                .add(key, data)
                .add("timestamp", System.currentTimeMillis())
                .build();
    }

}
