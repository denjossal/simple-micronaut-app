package example.micronaut.repository.service;

import example.micronaut.repository.entity.Counter;
import io.micronaut.core.annotation.NonNull;

import java.util.Optional;


public interface CounterRepositoryService {
    @NonNull
    Optional<Counter> findById();

    @NonNull
    Optional<Counter> save();

}
