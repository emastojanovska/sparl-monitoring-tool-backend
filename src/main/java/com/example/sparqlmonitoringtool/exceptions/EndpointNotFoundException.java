package com.example.sparqlmonitoringtool.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"stackTrace", "cause", "suppressed"})

public class EndpointNotFoundException extends RuntimeException {
    public EndpointNotFoundException(){
        super();
    }
}
