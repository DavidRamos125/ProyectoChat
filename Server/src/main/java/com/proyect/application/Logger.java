package com.proyect.application;

import com.proyect.util.Config;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final String logFile;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static Logger instance;

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    private Logger() {
        this.logFile = Config.getProperty("logFile");
    }

    /**
     * Método simplificado para loguear acciones con ubicación
     * @param accion La acción realizada
     * @param ubicacion La clase/método donde se ejecutó la acción
     */
    public synchronized void logAccion(String accion, String ubicacion) {
        String timestamp = LocalDateTime.now().format(formatter);
        String mensaje = String.format("[%s] [%s] %s", timestamp, ubicacion, accion);

        System.out.println(mensaje);

        try (FileWriter fw = new FileWriter(logFile, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(mensaje);
        } catch (IOException e) {
            System.err.println("Error escribiendo en el log: " + e.getMessage());
        }
    }
}
