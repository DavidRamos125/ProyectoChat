package com.proyect.server.infrastructure.service;

import com.proyect.server.infrastructure.entity.EFileContent;
import com.proyect.server.infrastructure.entity.EUser;
import com.proyect.server.infrastructure.repository.FileContentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileContentRepository fileContentRepository;

    public FileService(FileContentRepository fileContentRepository) {
        this.fileContentRepository = fileContentRepository;
    }

    public List<EFileContent> listAllFiles() {
        return fileContentRepository.findAllOrderBySizeDesc();
    }

    public List<EFileContent> listFilesByOwner(EUser owner) {
        return fileContentRepository.findByOwner(owner);
    }

    public Optional<EFileContent> findById(Integer id) {
        return fileContentRepository.findById(id);
    }

    public EFileContent save(EFileContent fileContent) {
        return fileContentRepository.save(fileContent);
    }
}
