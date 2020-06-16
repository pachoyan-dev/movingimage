package com.pachoyan.sensors;

import java.time.OffsetDateTime;
import java.util.function.Predicate;

public interface Sensor {
    Status status();

    default SensorStatistics statisticsLast30Days() {
        return new Last30DaysSensorStatistics(measurements().statisticsFor(last30Days()));
    }

    void register(Measurement measurement);

    Alerts alerts();

    Measurements measurements();

    static Predicate<Measurement> last30Days() {
        return measurement -> measurement.time().isAfter(OffsetDateTime.now().minusDays(30));
    }

    enum Status {
        OK,
        WARN,
        ALERT
    }
}
