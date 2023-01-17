package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findAllByURL(String url);
    Endpoint findByURL(String url);
    @Query("SELECT e FROM Endpoint e WHERE e.VoID = false")
    List<Endpoint> findAllWithoutVoid();
}
