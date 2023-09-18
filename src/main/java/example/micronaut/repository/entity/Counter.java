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

package example.micronaut.repository.entity;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigInteger;

@NotBlank
public class Counter {

    @NonNull
    @NotBlank
    private final String id;

    @NonNull
    @NotBlank
    private final BigInteger countNumber;

    @NonNull
    @NotBlank
    private final Integer c;

    public Counter(String id, BigInteger countNumber, Integer c) {
        this.id = id;
        this.countNumber = countNumber;
        this.c = c;
    }

    public String getId() {
        return id;
    }

    public BigInteger getCountNumber() {
        return countNumber;
    }

    public Integer getC() {
        return c;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "id='" + id + '\'' +
                ", countNumber=" + countNumber +
                ", c=" + c +
                '}';
    }
}
