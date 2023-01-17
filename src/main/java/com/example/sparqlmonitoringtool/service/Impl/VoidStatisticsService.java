package com.example.sparqlmonitoringtool.service.Impl;

import com.example.sparqlmonitoringtool.exceptions.EndpointNotFoundException;
import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.db.ResultValue;
import com.example.sparqlmonitoringtool.model.db.VoidDatasetStatistics;
import com.example.sparqlmonitoringtool.model.dto.ResultValueDTO;
import com.example.sparqlmonitoringtool.repository.EndpointRepository;
import com.example.sparqlmonitoringtool.repository.ResultValueRepository;
import com.example.sparqlmonitoringtool.repository.VoidStatisticsRepository;
import com.example.sparqlmonitoringtool.service.IVoidStatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VoidStatisticsService implements IVoidStatisticsService {
    public static boolean error ;
    private final VoidStatisticsRepository voidStatisticsRepository;
    private final ResultValueRepository resultValueRepository;
    private final EndpointRepository endpointRepository;

    public VoidStatisticsService(VoidStatisticsRepository voidStatisticsRepository, ResultValueRepository resultValueRepository, EndpointRepository endpointRepository) {
        this.voidStatisticsRepository = voidStatisticsRepository;
        this.resultValueRepository = resultValueRepository;
        this.endpointRepository = endpointRepository;
    }

    public String getVoidStatisticsAsTtl(Long id){
        Endpoint endpoint = endpointRepository.findById(id).orElseThrow(EndpointNotFoundException::new);
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointId(id);
        List<ResultValue> resultValues = resultValueRepository.findAllByVoidDatasetStatisticsId(voidDatasetStatistics.getId());
        String serviceDescription = "@prefix sd: <http://www.w3.org/ns/sparql-service-description#> .\n" +
                "@prefix void: <http://rdfs.org/ns/void#> .\n" +
                "\n" +
                "// SPARQL Service Description\n" +
                "\n" +
                "<" + endpoint.getURL() +"> a sd:Service ;\n" +
                "    sd:endpoint <"+ endpoint.getURL() +"> ;\n" +
                "    sd:defaultDataset [\n" +
                "        a sd:Dataset ;\n" +
                "        sd:defaultGraph [\n" +
                "            a sd:Graph ;\n" +
                "            void:triples" + voidDatasetStatistics.getNumTriples() + "\n" +
                "        ]\n" +
                "    ] .\n\n";
        String ttlResult = serviceDescription + "//VoID Description \n\n@prefix void: <http://vocab.deri.ie/void#> .\n\n<" + endpoint.getURL() + "> ";
        for (ResultValue resultValue : resultValues) {
            ttlResult += "\n\t" + resultValue.getName() + "\n\t\t" + resultValue.getValue() + "^^<" + resultValue.getDatatype() + "> ; \n";
        }
        ttlResult = ttlResult.trim();
        ttlResult += "\n\tvoid:exampleResource\n\t\t" + voidDatasetStatistics.getResource1() + "> ; \n";
        ttlResult += "\n\tvoid:exampleResource\n\t\t" + voidDatasetStatistics.getResource2() + "> ; \n";
        ttlResult += "\n\tvoid:exampleResource\n\t\t" + voidDatasetStatistics.getResource3() + "> .\n";
        return ttlResult;
    }



    @Override
    public String getVoidStatisticsAsRdfXml(Long id) {
        Endpoint endpoint = endpointRepository.findById(id).orElseThrow(EndpointNotFoundException::new);
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointId(id);
        List<ResultValue> resultValues = resultValueRepository.findAllByVoidDatasetStatisticsId(voidDatasetStatistics.getId());
        String serviceDescription = "<!--SPARQL Service Description-->\n" +
                "\n" +
                "<sd:Service>\n" +
                "    <sd:endpoint rdf:resource=\"" + endpoint.getURL() + "\"/>\n" +
                "    <sd:supportedLanguage rdf:resource=\"http://www.w3.org/ns/sparql-service-description#SPARQL11Query\"/>\n" +
                "    <sd:resultFormat rdf:resource=\"http://www.w3.org/ns/formats/RDF_XML\"/>\n" +
                "    <sd:resultFormat rdf:resource=\"http://www.w3.org/ns/formats/Turtle\"/>\n" +
                "\t<sd:defaultDataset>\n" +
                "      \t    <sd:Dataset>\n" +
                "       \t        <sd:defaultGraph>\n" +
                "          \t    <sd:Graph>\n" +
                "            \t        <void:triples rdf:datatype=\"http://www.w3.org/2001/XMLSchema#integer\">" + voidDatasetStatistics.getNumTriples() + "</void:triples>\n" +
                "                    </sd:Graph>\n" +
                "                </sd:defaultGraph>\n" +
                "\t    </sd:Dataset>\n" +
                "\t</sd:defaultDataset>\n" +
                "</sd:Service>\n";
        String rdfXmlResult = "<!--VoID Description--> \n\n<?xml version=\"1.0\"?>\n\n<rdf:RDF\n" +
                "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
                "xmlns:void=\"http://vocab.deri.ie/void#\">\n\n<rdf:Description rdf:about=\"" +
                endpoint.getURL() +
                "\">\n";
        for (ResultValue resultValue : resultValues) {
            String val = "\t<" + resultValue.getName() + ">" + resultValue.getValue() + "</" + resultValue.getName() + ">\n";
            rdfXmlResult += val;
        }
        rdfXmlResult += "<void:exampleResource>" + voidDatasetStatistics.getResource1() + "</void:exampleResource>\n" +
                " <void:exampleResource>" + voidDatasetStatistics.getResource2() + "</void:exampleResource>\n" +
                " <void:exampleResource>" + voidDatasetStatistics.getResource3() + "</void:exampleResource>\n";
        rdfXmlResult += "</rdf:Description>\n\n" + serviceDescription + "</rdf:RDF>";

        System.out.println("rdfXmlResult => " + rdfXmlResult);
        return rdfXmlResult;
    }

    @Override
    public String getVoidStatisticsAsJson(Long id) throws JsonProcessingException {
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointId(id);
        List<ResultValue> resultValues = resultValueRepository.findAllByVoidDatasetStatisticsId(voidDatasetStatistics.getId());
        String jsonResult = "{ " +
                "\"exampleResources\": {" +
                " \"type\": \"literal\",\n" +
                "    \"datatype\": \"http://www.w3.org/2001/XMLSchema#string\",\n" +
                "    \"value\": [\""+ voidDatasetStatistics.getResource1() + "\",\n"
        + "\"" + voidDatasetStatistics.getResource2() + "\",\n" + "\"" + voidDatasetStatistics.getResource3() + "\"]},";

        for (ResultValue resultValue : resultValues) {
            String valPart = "\"" + resultValue.getKeyName() + "\"" + ": " + resultValue.getJsonValue() + ",";
            String val = valPart.substring(0, valPart.length() - 2) + ", \"link\" : \"" + resultValue.getLink() + "\", \"name\": \"" + resultValue.getName() + "\"},";
            jsonResult += val;
        }
        jsonResult = jsonResult.substring(0, jsonResult.length() - 1);
        jsonResult += " } ";
        ObjectMapper mapper = new ObjectMapper();
        Object jsonString = mapper.readValue(jsonResult, Object.class);
        String indentedString = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(jsonString);
        return indentedString;
    }

    @Override
    public List<ResultValueDTO> getResultValuesByEndpoint(Long id) {
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointId(id);
        List<ResultValueDTO> resultValues = resultValueRepository.findAllByVoidDatasetStatisticsId(voidDatasetStatistics.getId())
                .stream()
                .map(resultValue -> mapToDTO(resultValue))
                .collect(Collectors.toList());
        return resultValues;
    }

    private ResultValueDTO mapToDTO(ResultValue resultValue){
        ResultValueDTO resultValueDTO = new ResultValueDTO();
        resultValueDTO.setValue(resultValue.getValue());
        resultValueDTO.setLink(resultValue.getLink());
        resultValueDTO.setDatatype(resultValue.getDatatype());
        resultValueDTO.setName(resultValue.getName());
        resultValueDTO.setType(resultValue.getType());
        return resultValueDTO;
    }

    public double getCoherenceValue(String endpointUrl, String namedGraph) {
        Set<String> types = getRDFTypes(namedGraph,endpointUrl);
        double weightedDenomSum = getTypesWeightedDenomSum(types,namedGraph,endpointUrl);
        double structuredness = 0;
        for(String type:types) {
            long occurenceSum = 0;
            Set<String> typePredicates = getTypePredicates(type,namedGraph,endpointUrl);
            long typeInstancesSize = getTypeInstancesSize(type,namedGraph,endpointUrl);
            for (String predicate:typePredicates)
            {
                long predicateOccurences = getOccurences(predicate,type,namedGraph,endpointUrl);
                occurenceSum = (occurenceSum + predicateOccurences);
            }
            double denom = typePredicates.size()*typeInstancesSize;
            if(typePredicates.size()==0)
                denom = 1;
            double coverage = occurenceSum/denom;
            double weightedCoverage = (typePredicates.size()+ typeInstancesSize) / weightedDenomSum;
            structuredness = (structuredness + (coverage*weightedCoverage));
        }
        return structuredness;
    }

    public static double getTypesWeightedDenomSum(Set<String> types, String namedGraph,String endpoint) {
        double sum = 0 ;
        for(String type:types)
        {
            long typeInstancesSize = getTypeInstancesSize(type,namedGraph, endpoint);
            long typePredicatesSize = getTypePredicates(type,namedGraph,endpoint).size();
            sum = sum + typeInstancesSize + typePredicatesSize;
        }
        return sum;
    }

    public static long getOccurences(String predicate, String type, String namedGraph, String endpoint)  {
        long predicateOccurences = 0  ;
        String queryString ;
        if(namedGraph ==null)
            queryString = "SELECT (Count(Distinct ?s) as ?occurences) \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s <"+predicate+"> ?o"
                    + "           }" ;
        else
            queryString = "SELECT (Count(Distinct ?s) as ?occurences) From <"+ namedGraph+"> \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s <"+predicate+"> ?o"
                    + "           }" ;
        System.out.println("Query string ==> " +  queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query);
        ResultSet res= qExe.execSelect();
        while(res.hasNext())
        {
            predicateOccurences = Long.parseLong(res.next().get("occurences").asLiteral().getString());
        }
        return predicateOccurences;

    }

    public static long getTypeInstancesSize(String type, String namedGraph,String endpoint)  {
        long typeInstancesSize =0;
        String queryString ;
        System.out.println("Type ==> " + type);
        System.out.println("Named graph ==> " + namedGraph);
        if(namedGraph ==null)
            queryString = "SELECT (Count(DISTINCT ?s)  as ?cnt ) \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s ?p ?o"
                    + "           }" ;
        else
            queryString = "SELECT (Count(DISTINCT ?s)  as ?cnt) From <"+ namedGraph+"> \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s ?p ?o"
                    + "           }" ;
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
        while(res.hasNext())
        {
            typeInstancesSize =  Long.parseLong(res.next().get("cnt").asLiteral().getString());
        }
        return typeInstancesSize;
    }

    public static Set<String> getTypePredicates(String type, String namedGraph,String endpoint)  {
        Set<String> typePredicates =new HashSet<String>() ;
        String queryString ;
        if(namedGraph ==null)
            queryString = "SELECT DISTINCT ?typePred \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s ?typePred ?o"
                    + "           }" ;
        else
            queryString = "SELECT DISTINCT ?typePred From <"+ namedGraph+"> \n"
                    + "			WHERE { \n"
                    + "            ?s a <"+type.replaceAll("\\s", "")+"> . "
                    + "            ?s ?typePred ?o"
                    + "           }" ;
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
        while(res.hasNext())
        {
            String predicate = res.next().get("typePred").toString();
            if (!predicate.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
                typePredicates.add(predicate);
        }
        return typePredicates;
    }


    public static Set<String> getRDFTypes(String namedGraph, String endpoint) {
        Set<String> types =new HashSet<String>() ;
        String queryString ="";
        if(namedGraph ==null)
            queryString = "SELECT DISTINCT ?type  \n"
                    + "			WHERE { \n"
                    + "            ?s a ?type"
                    + "           }" ;
        else
            queryString = "SELECT DISTINCT ?type From <"+ namedGraph+"> \n"
                    + "			WHERE { \n"
                    + "            ?s a ?type"
                    + "           }" ;
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
        while(res.hasNext())
        {
            types.add(res.next().get("type").toString());
        }
        return types;
    }

    public double getRelationshipSpecialty(String endpoint, String namedGraph) {
        Set<String> predicates = getRelationshipPredicates(endpoint, namedGraph);
        VoidDatasetStatistics voidDatasetStatistics = voidStatisticsRepository.findByEndpointURL(endpoint);
        System.out.println("Total Relationship Predicates to process: "+ predicates.size());
        long datasetSize = voidDatasetStatistics.getNumTriples();
        System.out.println("Total triples: "+ datasetSize);
        long subjects = voidDatasetStatistics.getNumSubjects();
        System.out.println("Total resources: "+ subjects);
        Kurtosis kurt = new Kurtosis();
        double relationshipSpecialty = 0 ;
        int i = 1;
        for (String predicate:predicates){
            error = false ;
            double [] occurences = getOccurences(predicate,endpoint,namedGraph,subjects);
            //	System.out.println(error);
            if(error==false){
                double kurtosis = kurt.evaluate(occurences);
                	System.out.println("Kurtosis: " +kurtosis);
                long tpSize = getPredicateSize(predicate,endpoint,namedGraph);
                	System.out.println("T.p Size: " +tpSize);
                relationshipSpecialty = relationshipSpecialty + (tpSize*kurtosis/datasetSize);
                System.out.println(i+": "+predicate+"\t Relationship Specialty so far: "+(relationshipSpecialty));
            }
            i++;
        }
        return relationshipSpecialty;
    }
    /**
     * Number of triples for a given predicate
     * @param predicate predicate
     * @param endpoint the endpoint url of the dataset
     * @param namedGraph graph name
     * @return #triples
     */
    public static long getPredicateSize(String predicate, String endpoint, String namedGraph)  {
        long count = 0;
        String queryString ="";
        try{
            if(namedGraph ==null)
                queryString = "SELECT (Count(*) as ?total) \n"
                        + "			WHERE { \n"
                        + "            ?s <"+predicate+"> ?o"
                        + "           }" ;
            else
                queryString = "SELECT  (Count(*) as ?total) From <"+ namedGraph+"> \n"
                        + "			WHERE { \n"
                        + "            ?s <"+predicate+"> ?o"
                        + "           }" ;
            //System.out.println(queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
            ResultSet res= qExe.execSelect();
            while(res.hasNext())
            {
                count = Long.parseLong(res.next().get("total").asLiteral().getString());
            }
        }catch(Exception e){return 0 ;}
        return count;
    }
    /**
     * Get occurences of the given predicate pertaining to the subject
     * @param predicate predicate
     * @param endpoint endpoint containing the dataset
     * @param namedGraph named graph
     * @param subjects number of subjects
     * @return
     */
    public static double[] getOccurences(String predicate, String endpoint, String namedGraph,long subjects) {

        double [] occurences = new double[(int) subjects+1];
        //System.out.println("Distribution sizue should be :" + occurences.length);
        String queryString ;   try{
            if(namedGraph ==null)
                queryString = "SELECT (count(?o) as ?occ) WHERE { ?res <"+predicate+"> ?o . } Group by ?res" ;
            else
                queryString = "SELECT (count(?o) as ?occ) From <"+ namedGraph+">  WHERE { ?res <"+predicate+"> ?o .  } Group by ?res  " ;

            //	System.out.println(predicate+" : "+queryString);
            Query query = QueryFactory.create(queryString);
            QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );

            ResultSet res= qExe.execSelect();
            int i = 0 ;
            while(res.hasNext()){
                occurences[i] = 	res.next().get("occ").asLiteral().getDouble();
                i++;
            }
            if (i==0)
                occurences[0] = 1;
            //	System.out.println("Distribution is ready");
        }catch (Exception ex){System.out.println(ex);error=true;}
        return occurences ;
    }
    /**
     * get the relationship predicates in a given dataset
     * @param endpoint endpoint url of the dataset
     * @param namedGraph graph name
     * @return set of relationship predicate
     */
    public static Set<String> getRelationshipPredicates(String endpoint,String namedGraph)  {
        Set<String> predicates =new HashSet<String>() ;
        String queryString ;
        if(namedGraph ==null)
            queryString = "SELECT DISTINCT ?p Where {?s ?p ?o . FILTER isIRI(?o) } " ;
        else
            queryString = "SELECT DISTINCT ?p From <"+ namedGraph+">  WHERE {?s ?p ?o . FILTER isIRI(?o) }" ;
        //System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService(endpoint, query );
        ResultSet res= qExe.execSelect();
        while(res.hasNext())
            predicates.add(res.next().get("p").toString());
        return predicates;
    }
}
