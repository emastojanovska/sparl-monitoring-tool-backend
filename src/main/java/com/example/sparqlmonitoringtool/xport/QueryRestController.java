package com.example.sparqlmonitoringtool.xport;

import com.example.sparqlmonitoringtool.model.db.Query;
import com.example.sparqlmonitoringtool.model.dto.EndpointQueriesDTO;
import com.example.sparqlmonitoringtool.model.dto.QueryDTO;
import com.example.sparqlmonitoringtool.model.dto.ResponseTimeAverage;
import com.example.sparqlmonitoringtool.service.IQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/queries")
public class QueryRestController {
    private final IQueryService queryService;

    public QueryRestController(IQueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/getQueryData")
    public String getQueryData(@RequestBody QueryDTO queryDTO){
        return queryService.getQueryData(queryDTO.getEndpointURL(), queryDTO.getQuery());
    }

    @GetMapping("/getAverageResponseTime")
    public Double getResponseTimeByQueryType(@RequestParam String type){
        return queryService.getAverageResponseTimeByQueryType(type);
    }

    @GetMapping("/getAllAverageResponseTimes")
    public List<ResponseTimeAverage> getAllResponseTimes(){
        return queryService.getAllAverageResponseTimes();
    }

    @GetMapping("/getQueryTypes")
    public List<String> getAllQueryTypes(){
        return queryService.getAllQueryTypes();
    }

    @GetMapping("/getAllByEndpoint")
    public List<EndpointQueriesDTO> getALlByEndpointId(){
        return queryService.getQueriesByEndpoint();
    }
}
