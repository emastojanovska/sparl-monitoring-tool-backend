package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.repository.InMemoryRepository;
import com.example.sparqlmonitoringtool.service.IInMemoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class InMemoryServiceImpl implements IInMemoryService {

    private final InMemoryRepository inMemoryRepository;

    public InMemoryServiceImpl(InMemoryRepository inMemoryRepository) {
        this.inMemoryRepository = inMemoryRepository;
    }

    @Override
    public HashMap<String, String> listAllQueries() {
        return inMemoryRepository.findAllQueries();
    }
}
