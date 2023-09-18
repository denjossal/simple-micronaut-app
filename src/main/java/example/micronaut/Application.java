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

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;

import static io.micronaut.context.env.Environment.FUNCTION;
import static io.micronaut.context.env.Environment.TEST;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }

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
}