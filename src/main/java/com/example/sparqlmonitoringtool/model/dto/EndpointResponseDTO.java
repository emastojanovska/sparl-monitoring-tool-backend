package com.example.sparqlmonitoringtool.model.dto;

import lombok.Data;

@Data
public class EndpointResponseDTO {
    private boolean down24h;
    private String serverName;
}
