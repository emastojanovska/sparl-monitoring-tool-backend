package com.example.sparqlmonitoringtool.model.events;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
public class EndpointCreatedEvent extends ApplicationEvent {
    private LocalDateTime when;
    public EndpointCreatedEvent(Object source) {
        super(source);
        this.when = LocalDateTime.now();
    }

    public EndpointCreatedEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
