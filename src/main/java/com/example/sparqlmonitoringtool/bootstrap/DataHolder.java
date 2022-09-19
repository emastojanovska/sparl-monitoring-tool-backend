package com.example.sparqlmonitoringtool.bootstrap;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class DataHolder {
    public static HashMap<String, String> queries = new HashMap<>();

    @PostConstruct
    public void init(){
        queries.put("SELECT", "SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1");
        queries.put("ASK", "ASK WHERE{ ?s ?p ?o . }");
        queries.put("SUBQUERY", "SELECT ?x ?y WHERE {\n" +
                "  VALUES ?x { 1 2 3 4 }\n" +
                "  {\n" +
                "    SELECT ?y WHERE { VALUES ?y { 5 6 7 8 }  }\n" +
                "  }\n" +
                "}");
        queries.put("AGGREGATE", "SELECT (COUNT(?person) AS ?people)\n" +
                "WHERE {\n" +
                "  ?person ?relation ?something .\n" +
                "} group by ?person");
        queries.put("NEGATION", "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "SELECT ?name\n" +
                "WHERE { ?x foaf:givenName  ?name .\n" +
                "\tOPTIONAL { ?x foaf:knows ?who } .\n" +
                "\tFILTER (!BOUND(?who)) \n" +
                "} ");
    }
}
