package example.micronaut.conf.dynamodb;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "dynamodb.table-name") // <1>
@ConfigurationProperties("dynamodb") // <2>
public interface DynamoConfiguration {
    @NotBlank
    String getTableName();
}
