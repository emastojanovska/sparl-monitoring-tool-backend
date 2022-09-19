package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {
    List<Endpoint> findByURL(String url);
}
