package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.exceptions.EndpointNotFoundException;
import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointQueriesDTO;
import com.example.sparqlmonitoringtool.model.dto.ResponseTimeAverage;
import com.example.sparqlmonitoringtool.repository.EndpointRepository;
import com.example.sparqlmonitoringtool.repository.InMemoryRepository;
import com.example.sparqlmonitoringtool.repository.QueryRepository;
import com.example.sparqlmonitoringtool.service.IQueryService;
import org.apache.jena.query.*;

import org.apache.jena.sparql.resultset.JSONOutput;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService implements IQueryService {
    private final QueryRepository queryRepository;
    private final InMemoryRepository inMemoryRepository;
    private final EndpointRepository endpointRepository;

    public QueryService(QueryRepository queryRepository, InMemoryRepository inMemoryRepository, EndpointRepository endpointRepository) {
        this.queryRepository = queryRepository;
        this.inMemoryRepository = inMemoryRepository;
        this.endpointRepository = endpointRepository;
    }

    @Override
    public Query createQuery(String endpointURL, String query) {
        return null;
    }

    @Override
    public void removeQuery(Long id) {

    }

    @Override
    public String getQueryData(String endpointURL, String queryText) {
        org.apache.jena.query.Query query = QueryFactory.create(queryText);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpointURL, query);

        ResultSet results = qexec.execSelect();
        JSONOutput jOut = new JSONOutput();
        String tmp = jOut.asString(results);
        qexec.close() ;
        return tmp;
     }

    @Override
    public Double getAverageResponseTimeByQueryType(String queryType) {
        List<Query> queries = queryRepository.findAllByType(queryType);
        double sumResponseTime = queries.stream()
                .map(x -> x.getResponseTime())
                .reduce(0.0, (i,j) -> i+j);
        double sumNumberExecuted = queries.stream()
                .mapToDouble(x -> x.getNumExecuted())
                .reduce(0.0, (i, j) -> i+j);
        BigDecimal avgResponseTime = new BigDecimal(sumResponseTime/sumNumberExecuted)
                .setScale(2, RoundingMode.HALF_UP);
        return avgResponseTime.doubleValue();
    }

    @Override
    public List<String> getAllQueryTypes() {
        List<String> types = new ArrayList<>();
        for(String type : inMemoryRepository.findAllQueries().keySet()){
            types.add(type);
        }
        return types;
    }

    @Override
    public List<ResponseTimeAverage> getAllAverageResponseTimes() {
        List<ResponseTimeAverage> responseTimeAverageList = new ArrayList<>();
        for (String query : getAllQueryTypes()){
            Double time = getAverageResponseTimeByQueryType(query);
            ResponseTimeAverage responseTimeAverage = new ResponseTimeAverage();
            responseTimeAverage.time = time;
            responseTimeAverage.type = query;
            responseTimeAverageList.add(responseTimeAverage);
        }
        return responseTimeAverageList;
    }

    @Override
    public List<EndpointQueriesDTO> getQueriesByEndpoint() {
        List<EndpointQueriesDTO> endpointQueriesDTOList = new ArrayList<>();
        for(Endpoint endpoint : endpointRepository.findAll())
        {
            EndpointQueriesDTO endpointQueriesDTO = new EndpointQueriesDTO();
            endpointQueriesDTO.endpointId = endpoint.getId();
            endpointQueriesDTO.endpointURL = endpoint.getURL();
            endpointQueriesDTO.queries = queryRepository.findAllByEndpoint(endpoint);
            endpointQueriesDTOList.add(endpointQueriesDTO);
        }
        return endpointQueriesDTOList;
    }
}
