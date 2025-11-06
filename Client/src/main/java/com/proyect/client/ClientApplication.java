package com.proyect.client;

import com.proyect.client.GUI.ContenedorPrincipal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ContenedorPrincipal cp = new ContenedorPrincipal();

        SpringApplication.run(ClientApplication.class, args);
    }

}
