package com.example.sparqlmonitoringtool.jobs;

import com.example.sparqlmonitoringtool.service.IEndpointService;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduledTasks {
    private final IEndpointService iEndpointService;

    public ScheduledTasks(IEndpointService iEndpointService) {
        this.iEndpointService = iEndpointService;
    }

    //Everyday at 6am
    @Scheduled(cron="0 0 6 * * *")
    //@Scheduled(cron="0 * * * * *")
    public void checkAvailability(){
        System.out.println("Calling updateAvailableEndpoints()");
        this.iEndpointService.updateAvailableEndpoints();
        System.out.println("Ending updateAvailableEndpoints()");
    }

    //@Scheduled(cron="0 * * * * *")
    @Scheduled(cron="0 0 6 * * *")
    public void checkResponseTime(){
        System.out.println("Calling updateResponseTime()");
        this.iEndpointService.updateResponseTime();
        System.out.println("Ending updateResponseTime()");
    }

    //Everyday at 6am
    @Scheduled(cron="0 0 6 * * *")
    public void getVoidStatistics() throws JSONException, IOException {
        System.out.println("Calling getVoidStatistics()");
        this.iEndpointService.updateVoidStatistics();
        System.out.println("Ending getVoidStatistics()");
    }


}
