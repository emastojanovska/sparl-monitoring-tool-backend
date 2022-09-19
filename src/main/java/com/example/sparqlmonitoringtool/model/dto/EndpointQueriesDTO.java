package com.example.sparqlmonitoringtool.model.dto;

import com.example.sparqlmonitoringtool.model.db.Query;

import java.util.List;

public class EndpointQueriesDTO {
    public Long endpointId;
    public String endpointURL;
    public List<Query> queries;
}
