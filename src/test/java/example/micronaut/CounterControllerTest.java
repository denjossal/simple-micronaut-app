/*
 * Copyright 2023 denjossal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.micronaut;

import example.micronaut.repository.entity.Counter;
import example.micronaut.repository.service.CounterRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS)
class CounterControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    CounterRepository counterRepository;

    private static HttpRequest<?> saveRequest() {
        return HttpRequest.GET("/counter");
    }

    @Test
    void testRetrieveBooks() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> saveResponse = client.exchange(saveRequest());
        assertEquals(HttpStatus.OK, saveResponse.status());
        Optional<Counter> result = counterRepository.findById();
        assertTrue(result.isPresent());
        assertEquals(BigInteger.ONE.intValue(), result.get().getC().intValue());
    }
}
