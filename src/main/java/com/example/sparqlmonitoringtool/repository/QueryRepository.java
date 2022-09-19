package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.db.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QueryRepository extends JpaRepository<Query, Long> {
    List<Query> findAllByEndpoint(Endpoint endpoint);
    List<Query> findAllByType(String type);
}
