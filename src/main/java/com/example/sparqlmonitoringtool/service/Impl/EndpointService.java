package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.exceptions.EndpointAlreadyExistsException;
import com.example.sparqlmonitoringtool.exceptions.EndpointNotFoundException;
import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointDTO;
import com.example.sparqlmonitoringtool.model.dto.EndpointResponseDTO;
import com.example.sparqlmonitoringtool.repository.EndpointRepository;
import com.example.sparqlmonitoringtool.repository.QueryRepository;
import com.example.sparqlmonitoringtool.service.IEndpointService;
import com.example.sparqlmonitoringtool.service.IInMemoryService;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.jena.ext.com.google.common.net.InternetDomainName;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EndpointService implements IEndpointService {

    private final EndpointRepository endpointRepository;
    private final IInMemoryService inMemoryService;
    private final QueryRepository queryRepository;

    public EndpointService(EndpointRepository endpointRepository, IInMemoryService inMemoryService, QueryRepository queryRepository) {
        this.endpointRepository = endpointRepository;
        this.inMemoryService = inMemoryService;
        this.queryRepository = queryRepository;
    }

    public boolean checkVoID(String endpointURL)throws IOException {
        String[] domainName = endpointURL.split("/");
        String checkVoid = "http://" + domainName[2] + "/.well-known/void";
        System.out.println("Void check ==> " + checkVoid);
        URL url = new URL(checkVoid.toLowerCase());
        System.out.println("Void url ==> " + checkVoid);

        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        int responseCode = huc.getResponseCode();
        System.out.println("Response code ==> " + responseCode);

        if(responseCode == 200)
            return true;
        else return false;

    }
    @Override
    public Endpoint createSparqlEndpoint(EndpointDTO endpointDTO) throws IOException, URISyntaxException {
        Endpoint endpoint = new Endpoint(endpointDTO.getURL());
        if(endpointRepository.findByURL(endpointDTO.getURL()).size() > 0)
        {
            throw new EndpointAlreadyExistsException();
        }else{
            EndpointResponseDTO dto = checkSparqlEndpointAvailabilityAndServer(endpoint.getURL());
            endpoint.setDown24h(dto.isDown24h());
            endpoint.setServerName(dto.getServerName());
            endpoint.setNumAvailable(0);
            endpoint.setNumUnavailable(0);
            if(endpointDTO.getName() != null || !endpointDTO.getName().isEmpty()){
                endpoint.setName(endpointDTO.getName());
            }else{
                endpoint.setName(endpointDTO.getURL());
            }
            endpoint.setVoID(checkVoID(endpoint.getURL()));
            endpointRepository.save(endpoint);
            addDefaultQueries(endpoint);
            return endpoint;
        }
    }

    private void addDefaultQueries(Endpoint endpoint){
        HashMap<String, String> queries = inMemoryService.listAllQueries();
        List<Query> queryList = new ArrayList<>();
        for (Map.Entry<String,String> entry : queries.entrySet()){
            Query query = new Query();
            query.setName(entry.getValue());
            query.setType(entry.getKey());
            query.setResponseTime(0.0);
            query.setEndpoint(endpoint);
            query.setNumExecuted(0);
            queryList.add(query);
            queryRepository.save(query);
        }
        endpoint.setQueries(queryList);
    }

    @Override
    public Endpoint removeSparqlEndpoint(Long id) {
        Endpoint endpoint = endpointRepository.findById(id).orElseThrow(EndpointNotFoundException::new);
        List<Query> queries = queryRepository.findAllByEndpoint(endpoint);
        queryRepository.deleteAll(queries);
        if(endpoint != null)
            endpointRepository.deleteById(id);
        return endpoint;
    }

    @Override
    public Endpoint getSparqlEndpointDetails(Long id) {
        return endpointRepository.findById(id).orElseThrow(EndpointNotFoundException::new);
    }

    @Override
    public List<Endpoint> getAllEndpoints() {
        return this.endpointRepository.findAll();
    }

    @Override
    public List<EndpointDTO> getAllEndpointsAsDTO() {
        return endpointRepository.findAll()
                .stream()
                .map(x -> convertToDTO(x))
                .collect(Collectors.toList());
    }

    private EndpointDTO convertToDTO(Endpoint endpoint){
        EndpointDTO endpointDTO = new EndpointDTO();
        String endpointURL = endpoint.getURL().replaceFirst("^https", "http");
        endpointDTO.setURL(endpointURL.toLowerCase());
        endpointDTO.setDown24h(endpoint.isDown24h());
        endpointDTO.setNewVersion(endpoint.isNewVersion());
        endpointDTO.setServerName(endpoint.getServerName());
        endpointDTO.setNumAvailable(endpoint.getNumAvailable());
        endpointDTO.setNumUnavailable(endpoint.getNumUnavailable());
        //Remove the next line if it takes much time
        endpointDTO.setAvailable(checkAvailable(endpoint.getURL()));
        endpointDTO.setVoID(endpoint.isVoID());

        return endpointDTO;
    }

    private EndpointResponseDTO checkSparqlEndpointAvailabilityAndServer(String endpointURL) throws IOException {
        endpointURL.replaceFirst("^https", "http");
        URL url = new URL(endpointURL.toLowerCase());
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        EndpointResponseDTO endpointResponseDTO = new EndpointResponseDTO();
        int responseCode = huc.getResponseCode();
        String server = huc.getHeaderField("Server");
        endpointResponseDTO.setServerName(server);
        System.out.println(responseCode);
        endpointResponseDTO.setDown24h(!checkAvailable(endpointURL));

        return endpointResponseDTO;
    }

    private boolean checkAvailable(String endpointURL) {
        String selectQuery = "SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1";
        org.apache.jena.query.Query queryToSelect = QueryFactory.create(selectQuery);
        QueryExecution queryExecutedSelect = QueryExecutionFactory.sparqlService(endpointURL, queryToSelect);
        return queryExecutedSelect.execSelect().hasNext();
    }

    public void updateAvailableEndpoints(){
        for (Endpoint endpoint: endpointRepository.findAll()) {
            if(checkAvailable(endpoint.getURL())){
                endpoint.setNumAvailable(endpoint.getNumAvailable() + 1);
                endpoint.setDown24h(false);
            }else{
                endpoint.setNumUnavailable(endpoint.getNumUnavailable() + 1);
                endpoint.setDown24h(true);
            }
            endpointRepository.save(endpoint);
        }
    }

    public static Long pingURL(String url, String query, int timeout) {
        url = url.replaceFirst("^https", "http");
        System.out.println("URL ---> " + url);
        try {
            StopWatch pageLoad = new StopWatch();
            pageLoad.start();
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            executeQuery(url, query);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            pageLoad.stop();
            Long pageLoadTime_ms = pageLoad.getTime();
            Long pageLoadTime_Seconds = pageLoadTime_ms / 1000;
            Long pageLoadTime_SecondsRemainder = (pageLoadTime_ms % 1000);
            System.out.println("Total Page Load Time: " + pageLoadTime_ms + " milliseconds");
            System.out.println("Total Page Load Time: " + "Seconds:" + pageLoadTime_Seconds + ", Remainder:" + pageLoadTime_SecondsRemainder);
            return pageLoadTime_ms;
        } catch (IOException exception) {
            return 0L;
        }
    }

    private static void executeQuery(String endpointURL, String query) {
        System.out.println("Query ---> " + query);
        org.apache.jena.query.Query queryToSelect = QueryFactory.create(query);
        QueryExecutionFactory.sparqlService(endpointURL, queryToSelect);
    }

    public void updateResponseTime(){
        for(Endpoint endpoint: endpointRepository.findAll()){
            for(Query query: queryRepository.findAllByEndpoint(endpoint)){
                Long responseTime = pingURL(endpoint.getURL(), query.getName(), 10);
                System.out.println("Response time --> " + responseTime);
                query.setResponseTime(query.getResponseTime() + responseTime);
                query.setNumExecuted(query.getNumExecuted() + 1);
                queryRepository.save(query);
            }
        }
    }


}
