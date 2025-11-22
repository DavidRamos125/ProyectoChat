package com.proyect.persistence;

import com.proyect.util.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GeneralConection implements Conection{
    private String url;
    private String user;
    private String password;
    private String driver;

    private Connection connection;
    private static GeneralConection instance;


    private GeneralConection(){
        String database = Config.getProperty("server.database");
        this.url = Config.getProperty(database + ".url");
        this.user = Config.getProperty(database + ".user");
        this.password = Config.getProperty(database + ".password");
        this.driver = Config.getProperty(database + ".driver");
    }

    public static GeneralConection getInstance(){
        if(instance == null){
            instance = new GeneralConection();
        }
        return instance;
    }

    @Override
    public Connection conect() {
        try {
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver JDBC de Oracle: " + driver, e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar: " + url, e);
        }
    }

    @Override
    public void desconect() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

}