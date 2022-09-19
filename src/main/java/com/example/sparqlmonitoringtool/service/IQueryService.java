package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointQueriesDTO;
import com.example.sparqlmonitoringtool.model.dto.ResponseTimeAverage;

import java.util.List;

public interface IQueryService {
    Query createQuery(String endpointURL, String query);
    void removeQuery(Long id);
    String getQueryData(String endpointURL, String query);
    Double getAverageResponseTimeByQueryType(String queryType);
    List<String> getAllQueryTypes();
    List<ResponseTimeAverage> getAllAverageResponseTimes();
    List<EndpointQueriesDTO> getQueriesByEndpoint();
}
