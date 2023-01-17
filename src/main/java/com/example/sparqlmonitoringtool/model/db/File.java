package com.example.sparqlmonitoringtool.model.db;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "name")
    private String fileName;

    @Column(name = "mime")
    private String mime;

    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;
}
