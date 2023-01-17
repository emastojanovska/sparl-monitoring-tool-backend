package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.model.db.ResultValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultValueRepository extends JpaRepository<ResultValue, Long> {
    List<ResultValue> findAllByVoidDatasetStatisticsId(Long id);
}

