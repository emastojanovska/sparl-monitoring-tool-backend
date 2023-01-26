package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.model.db.*;
import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointQueriesDTO;
import com.example.sparqlmonitoringtool.model.dto.ResponseTimeAverage;
import com.example.sparqlmonitoringtool.repository.*;
import com.example.sparqlmonitoringtool.service.IQueryService;
import com.example.sparqlmonitoringtool.service.IVoidStatisticsService;
import org.apache.jena.query.*;
import org.apache.jena.sparql.resultset.JSONOutput;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryService implements IQueryService {
    private final QueryRepository queryRepository;
    private final InMemoryRepository inMemoryRepository;
    private final EndpointRepository endpointRepository;
    private final VoidStatisticsRepository voidStatisticsRepository;
    private final ResultValueRepository resultValueRepository;
    private final IVoidStatisticsService voidStatisticsService;
    private final FileRepository fileRepository;
    public static String FILES_PATH = "C:\\Users\\Dell\\wbs\\sparqlMonitoringTool\\src\\main\\resources\\static\\files\\";

    public QueryService(QueryRepository queryRepository, InMemoryRepository inMemoryRepository, EndpointRepository endpointRepository, VoidStatisticsRepository voidStatisticsRepository, ResultValueRepository resultValueRepository, IVoidStatisticsService voidStatisticsService, FileRepository fileRepository) {
        this.queryRepository = queryRepository;
        this.inMemoryRepository = inMemoryRepository;
        this.endpointRepository = endpointRepository;
        this.voidStatisticsRepository = voidStatisticsRepository;
        this.resultValueRepository = resultValueRepository;
        this.voidStatisticsService = voidStatisticsService;
        this.fileRepository = fileRepository;
    }

    public void getVoidStatistics(String endpointURL) throws JSONException, IOException {
        getExampleResources(endpointURL);
        getNumberOfTriples(endpointURL);
        getNumberOfProperties(endpointURL);
        getNumberOfClasses(endpointURL);
        getNumberOfObjects(endpointURL);
        getNumberOfEntities(endpointURL);
        getNumberOfSubjects(endpointURL);
        generateFile(endpointURL, "json");
        generateFile(endpointURL, "ttl");
        generateFile(endpointURL, "rdf");
    }

    public void calculateCoherenceValue(String endpointUrl) {
        Endpoint endpoint = endpointRepository.findByURL(endpointUrl);
        double coherence = voidStatisticsService.getCoherenceValue(endpointUrl, null);
        endpoint.setCoherence(coherence);
        endpointRepository.save(endpoint);
    }

    @Override
    public void calculateRelationshipSpeciality(String endpointUrl) {
        Endpoint endpoint = endpointRepository.findByURL(endpointUrl);
        double kurtosis = voidStatisticsService.getRelationshipSpecialty(endpointUrl, null);
        endpoint.setRelationSpeciality(kurtosis);
        endpointRepository.save(endpoint);
    }

    @Override
    public void getExampleResources(String endpointUrl) throws JSONException {
        String exampleResourcesQuery = "SELECT distinct ?res where { ?res a ?class . FILTER regex(str(?res), \"resource\") .\n" +
                "} LIMIT 3";
        String result = getQueryData(endpointUrl, exampleResourcesQuery);
        String[] getArr = result.split("\\[");
        String[] getArrValue = getArr[2].split("\\{");
        String valueObject = "{" + getArrValue[2].split("\\}")[0] + "}";
        String valueObject1 = "{" + getArrValue[4].split("\\}")[0] + "}";
        String valueObject2 = "{" + getArrValue[6].split("\\}")[0] + "}";
        JSONArray array = new JSONArray("[" + valueObject + "]");
        JSONArray array1 = new JSONArray("[" + valueObject1 + "]");
        JSONArray array2 = new JSONArray("[" + valueObject2 + "]");
        JSONObject example1 = array.getJSONObject(0);
        JSONObject example2 = array1.getJSONObject(0);
        JSONObject example3 = array2.getJSONObject(0);
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointUrl);
        voidDatasetStatistics.setResource1(example1.getString("value"));
        voidDatasetStatistics.setResource2(example2.getString("value"));
        voidDatasetStatistics.setResource3(example3.getString("value"));
        voidStatisticsRepository.save(voidDatasetStatistics);

    }

    private void generateFile(String endpointUrl, String mimeType) throws IOException {
        Endpoint endpoint = endpointRepository.findByURL(endpointUrl);
        File file = new File();
        String data;
        switch (mimeType){
            case "json" : {
                data = voidStatisticsService.getVoidStatisticsAsJson(endpoint.getId());
                break;
            }
            case "ttl": {
                data = voidStatisticsService.getVoidStatisticsAsTtl(endpoint.getId());
                break;
            }
            case "rdf": {
                data = voidStatisticsService.getVoidStatisticsAsRdfXml(endpoint.getId());
                break;
            }
            default: data = voidStatisticsService.getVoidStatisticsAsJson(endpoint.getId());
        }
        String fileName = endpoint.getName().toLowerCase() + "-void." + mimeType;
        String filePath = FILES_PATH + fileName;
        file.setPath(filePath);
        file.setMime(mimeType);
        file.setFileName(fileName);
        fileRepository.save(file);
        file.setEndpoint(endpoint);
        File fileToDelete = fileRepository.findByFileName(fileName);
        if(fileToDelete != null){
            fileRepository.delete(fileToDelete);
        }
        fileRepository.save(file);
        FileOutputStream outputStream = new FileOutputStream(filePath);
        byte[] strToBytes = data.getBytes();
        outputStream.write(strToBytes);
        endpoint.setVoidFileGenerated(true);
        endpointRepository.save(endpoint);
        outputStream.close();
    }

    public void getNumberOfTriples(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("TRIPLES"));
        ResultValue resultValue = getJsonValue(resultAsString);
        Integer numberTriples = resultValue.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumTriples(numberTriples);
        voidStatisticsRepository.save(voidDatasetStatistics);
        resultValue.setName("void:triples");
        resultValue.setKeyName("triples");
        resultValue.setLink("http://vocab.deri.ie/void#triples");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValueRepository.save(resultValue);
    }

    public void getNumberOfEntities(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("ENTITIES"));
        ResultValue resultValue = getJsonValue(resultAsString);
        Integer numberEntities = resultValue.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumEntities(numberEntities);
        voidStatisticsRepository.save(voidDatasetStatistics);
        resultValue.setName("void:entities");
        resultValue.setKeyName("entities");
        resultValue.setLink("http://vocab.deri.ie/void#entities");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValueRepository.save(resultValue);
    }

    public void getNumberOfSubjects(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("SUBJECTS"));
        ResultValue resultValue = getJsonValue(resultAsString);
        Integer numberSubjects = resultValue.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumSubjects(numberSubjects);
        voidStatisticsRepository.save(voidDatasetStatistics);
        resultValue.setName("void:distinctSubjects");
        resultValue.setKeyName("subjects");
        resultValue.setLink("http://vocab.deri.ie/void#distinctSubjects");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValueRepository.save(resultValue);
    }

    public void getNumberOfProperties(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("PROPERTIES"));
        ResultValue resultValue = getJsonValue(resultAsString);
        Integer numberProperties = resultValue.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumProperties(numberProperties);
        voidStatisticsRepository.save(voidDatasetStatistics);
        resultValue.setName("void:properties");
        resultValue.setKeyName("properties");
        resultValue.setLink("http://vocab.deri.ie/void#properties");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValueRepository.save(resultValue);
    }

    public void getNumberOfObjects(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("OBJECTS"));
        ResultValue resultValue = getJsonValue(resultAsString);
        Integer numberObjects = resultValue.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumObjects(numberObjects);
        voidStatisticsRepository.save(voidDatasetStatistics);
        resultValue.setName("void:distinctObjects");
        resultValue.setKeyName("objects");
        resultValue.setLink("http://vocab.deri.ie/void#distinctObjects");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValueRepository.save(resultValue);
    }

    public void getNumberOfClasses(String endpointURL) throws JSONException {
        String resultAsString = getQueryData(endpointURL, inMemoryRepository.findVoidQueryByKey("CLASSES"));
        ResultValue result = getJsonValue(resultAsString);

        Integer numberClasses = result.getValue();
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpointURL);
        voidDatasetStatistics.setNumClasses(numberClasses);
        voidStatisticsRepository.save(voidDatasetStatistics);
        ResultValue resultValue = new ResultValue();
        resultValue.setValue(numberClasses);
        resultValue.setType(result.getType());
        resultValue.setName("void:classes");
        resultValue.setKeyName("classes");
        resultValue.setLink("http://vocab.deri.ie/void#classes");
        resultValue.setVoidDatasetStatistics(voidDatasetStatistics);
        resultValue.setDatatype(result.getDatatype());
        String jsonValue = "{ \"type\": \"" + resultValue.getType() + "\" , \"datatype\": \""
                + resultValue.getDatatype() + "\", \"value\": \"" + resultValue.getValue().toString() + "\"}";
        resultValue.setJsonValue(jsonValue);
        resultValueRepository.save(resultValue);
    }

    private ResultValue getJsonValue(String resultAsString) throws JSONException {
        ResultValue resultValue = new ResultValue();
        String[] getArr = resultAsString.split("\\[");
        String[] getArrValue = getArr[2].split("\\{");
        String valueObject = "{" + getArrValue[2].split("\\}")[0] + "}";
        resultValue.setJsonValue(valueObject);
        JSONArray array = new JSONArray("[" + valueObject + "]");
        JSONObject object = array.getJSONObject(0);
        resultValue.setValue(Integer.valueOf(object.getString("value")));
        resultValue.setType(object.getString("type"));
        resultValue.setDatatype(object.getString("datatype"));
        return resultValue;
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
