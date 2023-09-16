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
    private final BigInteger c;

    public Counter(String id, BigInteger countNumber, BigInteger c) {
        this.id = id;
        this.countNumber = countNumber;
        this.c = c;
    }

    public String getId() {
        return id;
    }

    public BigInteger getCountNumber(){
        return countNumber;
    }

    public BigInteger getC() { return c; }

    @Override
    public String toString() {
        return "Counter{" +
                "id='" + id + '\'' +
                ", countNumber=" + countNumber +
                ", c=" + c +
                '}';
    }
}
