package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointQueriesDTO;
import com.example.sparqlmonitoringtool.model.dto.ResponseTimeAverage;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IQueryService {
    Query createQuery(String endpointURL, String query);
    void removeQuery(Long id);
    String getQueryData(String endpointURL, String query) throws JSONException;
    Double getAverageResponseTimeByQueryType(String queryType);
    List<String> getAllQueryTypes();
    List<ResponseTimeAverage> getAllAverageResponseTimes();
    List<EndpointQueriesDTO> getQueriesByEndpoint();
    void getVoidStatistics(String endpointURL) throws JSONException, IOException;
    void calculateCoherenceValue(String endpointUrl);
    void calculateRelationshipSpeciality(String endpointUrl);
    void getExampleResources(String endpointUrl) throws JSONException;
}
