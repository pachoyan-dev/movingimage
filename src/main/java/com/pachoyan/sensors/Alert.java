package com.pachoyan.sensors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Alert {
    private final OffsetDateTime startDate;
    private final OffsetDateTime endDate;
    private final List<Measurement> measurements;

    public Alert(LinkedList<Measurement> measurements) {
        this.startDate = measurements.getFirst().time();
        this.endDate = measurements.getLast().time();
        this.measurements = measurements;
    }

    @JsonProperty("startDate")
    public OffsetDateTime startDate() {
        return startDate;
    }

    @JsonProperty("endDate")
    public OffsetDateTime endDate() {
        return endDate;
    }

    @JsonProperty("measurements")
    public List<Integer> measurements() {
        return measurements.stream()
                .map(Measurement::co2)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(startDate, alert.startDate) &&
                Objects.equals(endDate, alert.endDate) &&
                Objects.equals(measurements, alert.measurements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, measurements);
    }
}
