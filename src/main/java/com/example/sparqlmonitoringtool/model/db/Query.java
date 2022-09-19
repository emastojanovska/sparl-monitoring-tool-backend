package com.example.sparqlmonitoringtool.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double responseTime;
    private Integer numExecuted;
    private String type;
    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;
}
