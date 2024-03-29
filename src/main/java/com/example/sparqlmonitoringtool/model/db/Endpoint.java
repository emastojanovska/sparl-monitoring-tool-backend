package com.example.sparqlmonitoringtool.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", nullable = false)
    private String URL;

    @Column(name = "name")
    private String name;

    @Column(name = "num_available")
    private Integer numAvailable;

    @Column(name = "num_unavailable")
    private Integer numUnavailable;

    @Column(name = "down")
    private boolean down24h;

    @Column(name = "available")
    private boolean available;

    @Column(name = "void_file")
    private boolean voidFileGenerated;

    @Column(name = "new_version")
    private boolean newVersion;

    @Column(name = "void")
    private boolean VoID;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "relation_speciality")
    private double relationSpeciality;

    @Column(name = "coherence")
    private double coherence;

    @JsonIgnore
    @OneToOne(mappedBy = "endpoint")
    private VoidDatasetStatistics voidDatasetStatistics;

    @JsonIgnore
    @OneToMany(mappedBy = "endpoint")
    private List<File> voidFiles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "endpoint")
    private List<Query> queries = new ArrayList<>();

    public Endpoint(String URL) {
        this.URL = URL;
    }

    public Endpoint() {

    }
}
