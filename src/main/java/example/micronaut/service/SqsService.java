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

package example.micronaut.service;

import example.micronaut.conf.CIAwsCredentialsProviderChainCondition;
import example.micronaut.conf.CIAwsRegionProviderChainCondition;
import example.micronaut.conf.sqs.SqsConfiguration;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;

@Requires(condition = CIAwsRegionProviderChainCondition.class)
@Requires(condition = CIAwsCredentialsProviderChainCondition.class)
@Requires(beans = {SqsConfiguration.class, SqsClient.class})
@Singleton
@Primary
public class SqsService {

    private static final Logger LOG = LoggerFactory.getLogger(SqsService.class);
    protected final SqsClient sqsClient;
    protected final SqsConfiguration sqsConfiguration;
    @Value("${sqs.queue-name}")
    private String queueName;

    public SqsService(SqsClient sqsClient, SqsConfiguration sqsConfiguration) {
        this.sqsClient = sqsClient;
        this.sqsConfiguration = sqsConfiguration;
    }

    public boolean existQueue() {
        LOG.info("Checking Queue [{}]", queueName);
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest
                .builder()
                .queueName(queueName)
                .build();


        try {
            sqsClient.getQueueUrl(getQueueUrlRequest);
            return true;
        } catch (QueueDoesNotExistException e) {
            return false;
        }

    }

    public void createQueue() {
        LOG.info("Creating Queue [{}]", queueName);
        CreateQueueRequest createQueueRequest = CreateQueueRequest
                .builder()
                .queueName(queueName)
                .build();

        sqsClient.createQueue(createQueueRequest);
    }
}
