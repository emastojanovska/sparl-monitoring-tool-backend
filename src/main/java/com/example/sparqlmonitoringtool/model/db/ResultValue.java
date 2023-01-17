package com.example.sparqlmonitoringtool.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ResultValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String datatype;
    private Integer value;
    private String name;
    @Column(name = "key_name")
    private String keyName;
    private String link;
    private String jsonValue;
    @ManyToOne
    @JoinColumn(name = "void_dataset_statistics_id")
    private VoidDatasetStatistics voidDatasetStatistics;
}
