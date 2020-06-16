package com.pachoyan.sensors;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

public class InMemoryMeasurements implements Measurements {
    private final LinkedList<Measurement> measurements;

    public InMemoryMeasurements(Measurement measurement) {
        this.measurements = new LinkedList<>();
        this.measurements.add(measurement);
    }

    @Override
    public LinkedList<Measurement> all() {
        return measurements;
    }

    @Override
    public void add(Measurement measurement) {
        measurements.add(measurement);
    }

    @Override
    public Optional<Measurement> last() {
        return Optional.ofNullable(measurements.getLast());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryMeasurements that = (InMemoryMeasurements) o;
        return Objects.equals(measurements, that.measurements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurements);
    }
}
