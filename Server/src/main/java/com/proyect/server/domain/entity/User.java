package com.proyect.server.domain.entity;

import java.util.Date;

public class User {
    private double id;
    private String name;

    public User(double id, String name) {
        this.id = id;
        this.name = name;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
