package example.micronaut;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS)
class CounterControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    private static HttpRequest<?> saveRequest() {
        return HttpRequest.GET("/counter");
    }

    @Test
    void testRetrieveBooks() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> saveResponse = client.exchange(saveRequest());
        assertEquals(HttpStatus.OK, saveResponse.status());
    }
}
