package com.proyect.persistence;

import java.sql.Connection;

public interface Conection {
    public Connection conect();
    public void desconect();
}
