package example.micronaut;

import example.micronaut.repository.service.CounterRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;

@Requires(property = "dynamodb-local.host")
@Requires(property = "dynamodb-local.port")
@Requires(env = Environment.TEST)
@Singleton
public class TestBootstrap implements ApplicationEventListener<StartupEvent> {

    private final CounterRepository counterRepository;

    public TestBootstrap(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public void onApplicationEvent(StartupEvent event) {
        if (!counterRepository.existsTable()) {
            counterRepository.createTable();
        }
    }
}
