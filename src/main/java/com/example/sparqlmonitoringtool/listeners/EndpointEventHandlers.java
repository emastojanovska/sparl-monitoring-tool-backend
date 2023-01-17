package com.example.sparqlmonitoringtool.listeners;

import com.example.sparqlmonitoringtool.model.db.Endpoint;
import com.example.sparqlmonitoringtool.model.events.EndpointCreatedEvent;
import com.example.sparqlmonitoringtool.service.IEndpointService;
import org.json.JSONException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

@Configuration
@EnableAsync
public class EndpointEventHandlers {
    private final IEndpointService endpointService;

    public EndpointEventHandlers(IEndpointService endpointService) {
        this.endpointService = endpointService;
    }

    @Async
    @EventListener
    public void onEndpointCreated(EndpointCreatedEvent event) throws JSONException, IOException {
        System.out.println("Creating endpoint ==> ");
        endpointService.createVoidStatistics((Endpoint) event.getSource());
        endpointService.generateCoherence((Endpoint) event.getSource());
        endpointService.generateRelationshipSpeciality((Endpoint) event.getSource());
    }
}
