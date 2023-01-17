package com.example.sparqlmonitoringtool.bootstrap;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class DataHolder {
    public static HashMap<String, String> queries = new HashMap<>();
    public static HashMap<String, String> voidQueries = new HashMap<>();

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

        voidQueries.put("TRIPLES", "SELECT (COUNT(?s) AS ?triples) WHERE { ?s ?p ?o }");
        voidQueries.put("PROPERTIES_OLD","prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns>\n" +
                "SELECT (count( distinct ?property) as ?properties)\n" +
                "WHERE {\n" +
                "?property a rdf:Property .\n" +
                "}");
        voidQueries.put("PROPERTIES", "SELECT (Count(DISTINCT ?p) as ?total)\n" +
                "where {?s ?p ?o .}");
        voidQueries.put("ENTITIES", "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT (count(distinct ?entity) as ?total)\n" +
                "WHERE {\n" +
                "  ?subclass rdfs:subClassOf ?c .\n" +
                "  ?entity rdf:type ?subclass .\n" +
                "}");
        voidQueries.put("CLASSES-OWL", "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "SELECT (count(distinct ?class) as ?total)\n" +
                "WHERE {\n" +
                "    ?class a owl:Class\n" +
                "}");
        voidQueries.put("CLASSES", "prefix owl: <http://www.w3.org/2002/07/owl#>\n" +
                "SELECT (count(distinct ?class) as ?total)\n" +
                "WHERE {\n" +
                "    [] a ?class \n" +
                "}");
        voidQueries.put("CLASSES-RDFS", "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "SELECT (count(distinct ?class) as ?total)\n" +
                "WHERE {\n" +
                "    ?class a rdfs:Class\n" +
                "}");
        voidQueries.put("SUBJECTS", "SELECT (Count(DISTINCT ?s) as ?total) \n"
                + "			WHERE { \n"
                + "            ?s ?p ?o"
                + "           }");
        voidQueries.put("OBJECTS", "SELECT (Count(DISTINCT ?o) as ?total) \n"
                + "			WHERE { \n"
                + "            ?s ?p ?o"
                + "           }");

    }
}
