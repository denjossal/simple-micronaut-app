package example.micronaut.controller;

import example.micronaut.service.counter.CounterService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.Collections;
import java.util.Map;

@ExecuteOn(TaskExecutors.IO)
@Controller("/counter")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @Get
    public Map<String, Object> index(){
        counterService.updateCounter();
        return Collections.singletonMap("message", "Hello World");
    }

}