package com.proyect.server.infrastructure.controller;

import com.proyect.server.infrastructure.entity.EFileContent;
import com.proyect.server.infrastructure.service.FileService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Listar archivos (ordenados por tamaño)
    @GetMapping
    public ResponseEntity<List<EFileContent>> listAllFiles() {
        return ResponseEntity.ok(fileService.listAllFiles());
    }

    // Descargar archivo
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
        return fileService.findById(id)
                .map(file -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .body(file.getData()))
                .orElse(ResponseEntity.notFound().build());
    }
}

