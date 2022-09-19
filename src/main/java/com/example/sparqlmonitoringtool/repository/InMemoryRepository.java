package com.example.sparqlmonitoringtool.repository;

import com.example.sparqlmonitoringtool.bootstrap.DataHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class InMemoryRepository {
    public HashMap<String, String> findAllQueries(){
        return DataHolder.queries;
    }
}
