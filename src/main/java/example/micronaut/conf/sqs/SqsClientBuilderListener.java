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

package example.micronaut.conf.sqs;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.context.exceptions.ConfigurationException;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;


@Requires(property = "localstack-local.host")
@Requires(property = "sqs-local.port")
@Requires(env = Environment.TEST)
@Singleton // <2>
@Primary
public
class SqsClientBuilderListener
        implements BeanCreatedEventListener<SqsClientBuilder> { // <3>


    private final URI endpoint;
    private final String accessKeyId;
    private final String secretAccessKey;

    SqsClientBuilderListener(@Value("${localstack-local.host}") String host, // <4>
                             @Value("${sqs-local.port}") String port) {
        try {
            this.endpoint = new URI("http://" + host + ":" + port);
        } catch (URISyntaxException e) {
            throw new ConfigurationException("sqs.endpoint not a valid URI");
        }
        this.accessKeyId = "fakeMyKeyId";
        this.secretAccessKey = "fakeSecretAccessKey";
    }

    @Override
    public SqsClientBuilder onCreated(BeanCreatedEvent<SqsClientBuilder> event) {
        return event.getBean().endpointOverride(endpoint)
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return accessKeyId;
                    }

                    @Override
                    public String secretAccessKey() {
                        return secretAccessKey;
                    }
                });
    }

}
