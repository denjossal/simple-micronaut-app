package example.micronaut;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;

import static io.micronaut.context.env.Environment.*;

public class Application {

    @ContextConfigurer
    public static class DefaultEnvironmentConfigurer implements ApplicationContextConfigurer {
        @Override
        public void configure(@NonNull ApplicationContextBuilder builder) {
            String env = System.getenv("STG");
            if (null != env && env.length() > 0) {
                builder.defaultEnvironments(FUNCTION);
            } else {
                builder.defaultEnvironments(TEST);
            }
        }
    }

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}