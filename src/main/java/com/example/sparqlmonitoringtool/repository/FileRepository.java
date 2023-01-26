package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {
    File findByEndpointId(Long id);
    @Query("SELECT f FROM File f WHERE f.endpoint.id = ?1 and f.mime = ?2")
    File findByEndpointIdAndMimeType(Long id, String format);

    File findByFileName(String fileName);
}
