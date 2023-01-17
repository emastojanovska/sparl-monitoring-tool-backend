package com.example.sparqlmonitoringtool.service;

import com.example.sparqlmonitoringtool.model.dto.ResultValueDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IVoidStatisticsService {
     String getVoidStatisticsAsJson(Long id) throws JsonProcessingException;
     String getVoidStatisticsAsTtl(Long id);
     String getVoidStatisticsAsRdfXml(Long id);
     List<ResultValueDTO> getResultValuesByEndpoint(Long id);
     double getCoherenceValue(String endpointUrl, String namedGraph);
     double getRelationshipSpecialty(String endpoint, String namedGraph);
}
