package com.example.sparqlmonitoringtool.xport;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.dto.EndpointDTO;
import com.example.sparqlmonitoringtool.service.IEndpointService;
import com.example.sparqlmonitoringtool.service.IQueryService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/endpoints")
public class EndpointRestController {
    private final IEndpointService iEndpointService;

    public EndpointRestController(IEndpointService iEndpointService) {
        this.iEndpointService = iEndpointService;
    }

    @GetMapping
    public List<Endpoint> getAllEndpoints(){
        return iEndpointService.getAllEndpoints();
    }

    @PostMapping("/create")
    public Endpoint addEndpoint(@RequestBody EndpointDTO endpointDTO) throws IOException, URISyntaxException {
        return iEndpointService.createSparqlEndpoint(endpointDTO);
    }

    @PostMapping("/remove")
    public Endpoint removeEndpoint(@RequestBody Endpoint endpoint){
        return iEndpointService.removeSparqlEndpoint(endpoint.getId());
    }

}
