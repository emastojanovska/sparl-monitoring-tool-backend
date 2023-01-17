package com.example.sparqlmonitoringtool.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class VoidDatasetStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_triples")
    private Integer numTriples;

    @Column(name = "num_classes")
    private Integer numClasses;

    @Column(name = "num_predicates")
    private Integer numPredicates;

    @Column(name = "num_objects")
    private Integer numObjects;

    @Column(name = "num_subjects")
    private Integer numSubjects;

    @Column(name = "num_entities")
    private Integer numEntities;

    @Column(name = "num_properties")
    private Integer numProperties;

    @Column(name = "ex_resource_1")
    private String resource1;

    @Column(name = "ex_resource_2")
    private String resource2;

    @Column(name = "ex_resource_3")
    private String resource3;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endpoint_id", referencedColumnName = "id")
    private Endpoint endpoint;

    @JsonIgnore
    @OneToMany(mappedBy = "voidDatasetStatistics")
    private List<ResultValue> resultValues = new ArrayList<>();

}
