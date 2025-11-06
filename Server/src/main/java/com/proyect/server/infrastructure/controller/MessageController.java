package com.proyect.server.infrastructure.controller;

import com.proyect.server.infrastructure.entity.EMessage;
import com.proyect.server.infrastructure.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Listar todos los mensajes
    @GetMapping
    public ResponseEntity<List<EMessage>> listAllMessages() {
        return ResponseEntity.ok(messageService.listAll());
    }

    // Mensajes enviados por usuario
    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<EMessage>> listSentMessages(@PathVariable Integer userId) {
        return ResponseEntity.ok(messageService.listMessagesSent(userId));
    }

    // Mensajes recibidos por usuario
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<EMessage>> listReceivedMessages(@PathVariable Integer userId) {
        return ResponseEntity.ok(messageService.listMessagesReceived(userId));
    }

    // Detalle de mensaje
    @GetMapping("/{id}")
    public ResponseEntity<EMessage> getMessageDetail(@PathVariable Integer id) {
        return messageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
