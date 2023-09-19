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

package example.micronaut.listener;

import example.micronaut.service.counter.CounterService;
import io.micronaut.jms.annotations.JMSListener;
import io.micronaut.jms.annotations.Queue;
import io.micronaut.messaging.annotation.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME;

@JMSListener(CONNECTION_FACTORY_BEAN_NAME)
public class SqsConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsConsumer.class);
    private static final ConcurrentLinkedQueue<String> globalQueue = new ConcurrentLinkedQueue<>();
    private final CounterService counterService;

    public SqsConsumer(CounterService counterService) {
        this.counterService = counterService;
    }

    @Queue(value = "counter-sqs")
    public void receive(@MessageBody String body) {
        LOGGER.info("Message has been consumed. Message body: {}", body);
        globalQueue.add(body);
        if (globalQueue.size() == 100) {
            counterService.saveRecords(globalQueue);
        }
    }
}