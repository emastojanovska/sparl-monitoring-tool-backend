package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.dto.EndpointDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface IEndpointService {
    Endpoint createSparqlEndpoint(EndpointDTO endpointDTO) throws IOException, URISyntaxException, JSONException;
    Endpoint removeSparqlEndpoint(Long id);
    Endpoint editSparqlEndpoint(Long id, String endpointName);
    Endpoint getEndpointById(Long id);
    Endpoint getSparqlEndpointDetails(Long id);
    List<Endpoint> getAllEndpoints();
    List<EndpointDTO> getAllEndpointsAsDTO();
    void updateAvailableEndpoints();
    void updateResponseTime();
    void updateVoidStatistics() throws JSONException, IOException;
    void createVoidStatistics(Endpoint endpoint) throws JSONException, IOException;
    void generateCoherence(Endpoint endpoint);
    void generateRelationshipSpeciality(Endpoint endpoint);
}
