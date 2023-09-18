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

package example.micronaut.repository.service;

import example.micronaut.conf.CIAwsCredentialsProviderChainCondition;
import example.micronaut.conf.CIAwsRegionProviderChainCondition;
import example.micronaut.conf.dynamodb.DynamoConfiguration;
import example.micronaut.repository.entity.Counter;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.math.BigInteger;
import java.util.*;

@Requires(condition = CIAwsRegionProviderChainCondition.class)
@Requires(condition = CIAwsCredentialsProviderChainCondition.class)
@Requires(beans = {DynamoConfiguration.class, DynamoDbClient.class})
@Primary
@Singleton
public class CounterRepository implements CounterRepositoryService {

    protected static final String ATTRIBUTE_PK = "pointer";
    protected static final String ATTRIBUTE_SK = "countNumber";
    private static final Logger LOG = LoggerFactory.getLogger(CounterRepository.class);
    private static final String TABLE_NAME = "counter";
    private static final String P_COLUMN = "pointer";
    private static final String P_COLUMN_VALUE = "key";
    private static final String S_COLUMN = "countNumber";
    private static final String S1_COLUMN = "c";
    protected final DynamoDbClient dynamoDbClient;
    protected final DynamoConfiguration dynamoConfiguration;
    public CounterRepository(DynamoDbClient dynamoDbClient,
                             DynamoConfiguration dynamoConfiguration) {
        this.dynamoDbClient = dynamoDbClient;
        this.dynamoConfiguration = dynamoConfiguration;
    }

    @Override
    public Optional<Counter> findById() {
        Map<String, AttributeValue> searchCriteria = new HashMap<>();
        searchCriteria.put(P_COLUMN, AttributeValue.builder().s(P_COLUMN_VALUE).build());
        String sColumnValue = "0";
        searchCriteria.put(S_COLUMN, AttributeValue.builder().n(sColumnValue).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(searchCriteria)
                .attributesToGet(List.of(P_COLUMN, S_COLUMN, S1_COLUMN))
                .build();

        GetItemResponse getItemResponse = dynamoDbClient.getItem(request);

        return Optional.of(
                new Counter(
                        String.valueOf(getItemResponse.item().get(P_COLUMN).s()),
                        new BigInteger(String.valueOf(getItemResponse.item().get(S_COLUMN).n())),
                        Integer.valueOf(String.valueOf(getItemResponse.item().get(S1_COLUMN).n()))));
    }

    @Override
    public Optional<Counter> save() {
        Counter stats = findById().orElseThrow();
        Counter newCounter = new Counter(stats.getId(), stats.getCountNumber(), stats.getC() + 1);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put(P_COLUMN, AttributeValue.builder().s(P_COLUMN_VALUE).build());
        item.put(S_COLUMN, AttributeValue.builder().n(newCounter.getCountNumber().toString()).build());
        item.put(S1_COLUMN, AttributeValue.builder().n(newCounter.getC().toString()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);

        return Optional.of(newCounter);
    }

    public boolean existsTable() {
        LOG.info("Checking table [{}]", TABLE_NAME);
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(dynamoConfiguration.getTableName())
                    .build());
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    public void createTable() {
        LOG.info("Creating table [{}]", TABLE_NAME);
        dynamoDbClient.createTable(CreateTableRequest.builder()
                .attributeDefinitions(AttributeDefinition.builder()
                                .attributeName(ATTRIBUTE_PK)
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName(ATTRIBUTE_SK)
                                .attributeType(ScalarAttributeType.N)
                                .build())
                .keySchema(Arrays.asList(KeySchemaElement.builder()
                                .attributeName(ATTRIBUTE_PK)
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName(ATTRIBUTE_SK)
                                .keyType(KeyType.RANGE)
                                .build()
                ))
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .tableName(dynamoConfiguration.getTableName())
                .build());
        createRecordCounter();
    }

    public void createRecordCounter() {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(P_COLUMN, AttributeValue.builder().s(P_COLUMN_VALUE).build());
        item.put(S_COLUMN, AttributeValue.builder().n("0").build());
        item.put(S1_COLUMN, AttributeValue.builder().n("0").build());

        LOG.info("Inserting 1st Record [{}]", item);

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }
}
