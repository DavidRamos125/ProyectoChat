package com.proyect.server.infrastructure.repository;


import com.proyect.server.infrastructure.entity.EFileContent;
import com.proyect.server.infrastructure.entity.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileContentRepository extends JpaRepository<EFileContent, Integer> {

    List<EFileContent> findByOwner(EUser owner);

    @Query("SELECT f FROM EFileContent f ORDER BY f.size DESC")
    List<EFileContent> findAllOrderBySizeDesc();

    @Query("SELECT f FROM EFileContent f WHERE f.size > :size")
    List<EFileContent> findFilesLargerThan(Long size);
}
