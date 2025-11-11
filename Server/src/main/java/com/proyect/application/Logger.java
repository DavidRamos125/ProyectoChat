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

    public synchronized void logAccion(String accion) {
        String timestamp = LocalDateTime.now().format(formatter);
        String mensaje = "[" + timestamp + "] " + accion;

        System.out.println(mensaje);

        try (FileWriter fw = new FileWriter(logFile, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(mensaje);
        } catch (IOException e) {
            System.err.println("Error escribiendo en el log: " + e.getMessage());
        }
    }
}
