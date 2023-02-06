package com.example.sparqlmonitoringtool.xport;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.db.File;
import com.example.sparqlmonitoringtool.model.dto.EndpointDTO;
import com.example.sparqlmonitoringtool.model.dto.ResultValueDTO;
import com.example.sparqlmonitoringtool.service.IEndpointService;
import com.example.sparqlmonitoringtool.service.IFileService;
import com.example.sparqlmonitoringtool.service.IVoidStatisticsService;
import org.json.JSONException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/endpoints")
public class EndpointRestController {
    private final IEndpointService iEndpointService;
    private final IVoidStatisticsService iVoidStatisticsService;
    private final IFileService iFileService;

    public EndpointRestController(IEndpointService iEndpointService, IVoidStatisticsService iVoidStatisticsService, IFileService iFileService) {
        this.iEndpointService = iEndpointService;
        this.iVoidStatisticsService = iVoidStatisticsService;
        this.iFileService = iFileService;
    }

    @GetMapping
    public List<Endpoint> getAllEndpoints(){
        return iEndpointService.getAllEndpoints();
    }

    @PostMapping("/create")
    public Endpoint addEndpoint(@RequestBody EndpointDTO endpointDTO) throws IOException, URISyntaxException, JSONException {
        return iEndpointService.createSparqlEndpoint(endpointDTO);
    }

    @PostMapping("/remove")
    public Endpoint removeEndpoint(@RequestBody Endpoint endpoint){
        return iEndpointService.removeSparqlEndpoint(endpoint.getId());
    }

    @PostMapping("/edit/{id}")
    public Endpoint editEndpoint(@PathVariable Long id, @RequestParam String endpointName){
        return iEndpointService.editSparqlEndpoint(id, endpointName);
    }

    @GetMapping("/void/{id}")
    public List<ResultValueDTO> voidStatisticsAsJson(@PathVariable Long id){
        return iVoidStatisticsService.getResultValuesByEndpoint(id);
    }

    @PostMapping("/download/{id}")
    public ResponseEntity<Resource> downloadVoidFile(@PathVariable Long id, @RequestParam String format) {
        File voidFile =  iFileService.getFileByEndpointAndMimeType(id, format);
        FileSystemResource resource = new FileSystemResource(voidFile.getPath());
        MediaType mediaType = MediaTypeFactory
                .getMediaType(resource)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ContentDisposition disposition = ContentDisposition
                .inline()
                .filename(voidFile.getFileName())
                .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity(resource, headers, HttpStatus.OK);
    }


}
