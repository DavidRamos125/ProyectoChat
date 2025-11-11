package com.proyectofinal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("system.properties")) {

            if (input == null) {
                System.err.println("No se encontró el archivo system.properties en resources/");
            } else {
                properties.load(input);
                System.out.println("Archivo system.properties cargado correctamente.");
            }

        } catch (IOException e) {
            System.err.println("Error cargando system.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
