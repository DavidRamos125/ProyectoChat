package com.proyect.server.domain;

import org.springframework.beans.factory.annotation.Value;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    @Value("${logger.file}")
    private String logFile;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
