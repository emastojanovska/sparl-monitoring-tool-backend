package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.VoidDatasetStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoidStatisticsRepository extends JpaRepository<VoidDatasetStatistics, Long> {

    VoidDatasetStatistics findByEndpointURL(String url);
    VoidDatasetStatistics findByEndpointId(Long id);
}
