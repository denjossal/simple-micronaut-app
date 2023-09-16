package example.micronaut.service.counter;

import example.micronaut.repository.service.CounterRepository;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Introspected
public class CounterServiceImpl implements CounterService {

    private static final Logger LOG = LoggerFactory.getLogger(CounterServiceImpl.class);

    private final CounterRepository counterRepository;

    public CounterServiceImpl(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }

    @Override
    public void updateCounter() {
        LOG.info("Updating counter....");
        LOG.info("CounterEntity {}", counterRepository.save().orElseThrow());
        LOG.info("Counter updated....");
    }
}
