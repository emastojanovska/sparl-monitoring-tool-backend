package com.example.sparqlmonitoringtool.model.dto;

import lombok.Data;

@Data
public class ResultValueDTO {
    private String type;
    private String datatype;
    private Integer value;
    private String name;
    private String link;
}
