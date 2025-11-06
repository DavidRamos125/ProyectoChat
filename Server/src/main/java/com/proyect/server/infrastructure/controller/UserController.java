package com.proyect.server.infrastructure.controller;


import com.proyect.server.infrastructure.entity.EUser;
import com.proyect.server.infrastructure.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Crear nuevo usuario (pendiente de aceptación)
    @PostMapping
    public ResponseEntity<EUser> createUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.createUser(username));
    }

    // Aceptar usuario (acción del servidor)
    @PutMapping("/{id}/accept")
    public ResponseEntity<EUser> acceptUser(@PathVariable Integer id) {
        return userService.acceptUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<EUser>> listAllUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    // Usuarios conectados
    @GetMapping("/connected")
    public ResponseEntity<List<EUser>> listConnectedUsers() {
        return ResponseEntity.ok(userService.listConnectedUsers());
    }

    // Usuarios desconectados
    @GetMapping("/disconnected")
    public ResponseEntity<List<EUser>> listDisconnectedUsers() {
        return ResponseEntity.ok(userService.listDisconnectedUsers());
    }

    // Información completa del usuario
    @GetMapping("/{id}/info")
    public ResponseEntity<?> getUserInfo(@PathVariable Integer id) {
        return userService.getUserInfo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
