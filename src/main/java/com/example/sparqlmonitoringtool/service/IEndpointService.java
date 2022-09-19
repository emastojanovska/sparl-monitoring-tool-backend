package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.dto.EndpointDTO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface IEndpointService {
    Endpoint createSparqlEndpoint(EndpointDTO endpointDTO) throws IOException, URISyntaxException;
    Endpoint removeSparqlEndpoint(Long id);
    Endpoint getSparqlEndpointDetails(Long id);
    List<Endpoint> getAllEndpoints();
    List<EndpointDTO> getAllEndpointsAsDTO();
    void updateAvailableEndpoints();
    void updateResponseTime();
}
