package com.example.sparqlmonitoringtool.model.dto;

import lombok.Data;

@Data
public class EndpointDTO {
    private String URL;
    private String name;
    private Integer numAvailable;
    private Integer numUnavailable;
    private boolean down24h;
    private boolean newVersion;
    private boolean VoID;
    private boolean available;
    private String serverName;
}
