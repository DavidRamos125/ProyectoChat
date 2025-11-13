package com.proyect;

import com.proyect.GUI.VentanaPrincipal;
import com.proyect.domain.FileContent;
import com.proyect.domain.Message;
import com.proyect.domain.TextContent;
import com.proyect.domain.User;
import com.proyect.factory.ExternalFactory;
import com.proyect.util.JSONUtil;

import java.util.Date;
import java.util.Arrays;


public class Main {

    public static void main(String[] args) {
        // 🔹 Crear usuarios de ejemplo
        User sender = new User();
        sender.setId(1);
        sender.setUsername("sender");
        sender.setPassword("password");

        User receiver = new User();
        receiver.setId(2);
        receiver.setUsername("receiver");
        receiver.setPassword("password");

        // 🔹 Crear mensaje de texto
        TextContent textContent = new TextContent();
        textContent.setText("¡Hola Bob, este es un mensaje de prueba!");

        Message textMessage = new Message();
        textMessage.setId(1);
        textMessage.setSender(sender);
        textMessage.setReceiver(receiver);
        textMessage.setContent(textContent);
        textMessage.setDate(new Date());

        // 🔹 Convertir mensaje de texto a JSON
        String jsonText = JSONUtil.objectToJSON(textMessage);
        System.out.println("=== MENSAJE DE TEXTO SERIALIZADO ===");
        System.out.println(jsonText);

        // 🔹 Volver a objeto desde JSON
        Message textMsgRestored = JSONUtil.JSONToObject(jsonText, Message.class);
        System.out.println("\n=== MENSAJE DE TEXTO DESERIALIZADO ===");
        System.out.println("Tipo de contenido: " + textMsgRestored.getContent().getClass().getSimpleName());
        System.out.println("Texto: " + ((TextContent) textMsgRestored.getContent()).getText());


        // 🔹 Crear mensaje con archivo
        byte[] fileData = {65, 66, 67, 68, 69}; // bytes = "ABCDE"
        FileContent fileContent = new FileContent("documento.txt", "abc123", fileData);

        Message fileMessage = new Message();
        fileMessage.setId(2);
        fileMessage.setSender(sender);
        fileMessage.setReceiver(receiver);
        fileMessage.setContent(fileContent);
        fileMessage.setDate(new Date());

        // 🔹 Convertir mensaje de archivo a JSON
        String jsonFile = JSONUtil.objectToJSON(fileMessage);
        System.out.println("\n=== MENSAJE DE ARCHIVO SERIALIZADO ===");
        System.out.println(jsonFile);

        // 🔹 Volver a objeto desde JSON
        Message fileMsgRestored = JSONUtil.JSONToObject(jsonFile, Message.class);
        System.out.println("\n=== MENSAJE DE ARCHIVO DESERIALIZADO ===");
        System.out.println("Tipo de contenido: " + fileMsgRestored.getContent().getClass().getSimpleName());
        FileContent restoredFile = (FileContent) fileMsgRestored.getContent();
        System.out.println("Nombre: " + restoredFile.getName());
        System.out.println("Tamaño: " + restoredFile.getSize());
        System.out.println("Datos: " + Arrays.toString(restoredFile.getData()));
        VentanaPrincipal ventanaPrincipal = ExternalFactory.getVentanaPrincipal();
        ventanaPrincipal.setVisible(true);
    }
}