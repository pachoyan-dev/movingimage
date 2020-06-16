package com.pachoyan.sensors;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.IntSummaryStatistics;
import java.util.Objects;

public class Last30DaysSensorStatistics implements SensorStatistics {
    private final int max;
    private final double avg;

    public Last30DaysSensorStatistics(IntSummaryStatistics statistics) {
        this.max = statistics.getMax();
        this.avg = statistics.getAverage();
    }

    @Override
    @JsonProperty("maxLast30Days")
    public int max() {
        return max;
    }

    @Override
    @JsonProperty("avgLast30Days")
    public double avg() {
        return avg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Last30DaysSensorStatistics that = (Last30DaysSensorStatistics) o;
        return max == that.max &&
                Double.compare(that.avg, avg) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(max, avg);
    }
}
