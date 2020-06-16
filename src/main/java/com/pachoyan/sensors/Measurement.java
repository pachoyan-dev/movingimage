package com.pachoyan.sensors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Measurement {
    private final int co2;
    private final OffsetDateTime time;

    @JsonCreator
    public Measurement(
            @JsonProperty("co2") int co2,
            @JsonProperty("time") OffsetDateTime time
    ) {
        this.co2 = co2;
        this.time = time;
    }

    public Integer co2() {
        return co2;
    }

    public OffsetDateTime time() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measurement that = (Measurement) o;
        return co2 == that.co2 &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(co2, time);
    }
}


