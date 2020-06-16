package com.pachoyan.sensors;

import java.time.OffsetDateTime;
import java.util.*;

public class InMemorySensor implements Sensor {
    private final Measurements measurements;
    private final Alerts alerts;
    private final SensorStatus sensorStatus;

    public InMemorySensor(Measurement measurement) {
        this.measurements = new InMemoryMeasurements(measurement);
        this.alerts = new InMemoryAlerts();
        this.sensorStatus = new SensorStatus(measurement);
    }

    @Override
    public Status status() {
        return sensorStatus.current();
    }

    @Override
    public void register(Measurement measurement) {
        measurements.add(measurement);
        sensorStatus.set(measurement);
    }

    @Override
    public Alerts alerts() {
        return alerts;
    }

    @Override
    public Measurements measurements() {
        return measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemorySensor that = (InMemorySensor) o;
        return Objects.equals(measurements, that.measurements) &&
                Objects.equals(alerts, that.alerts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measurements, alerts);
    }

    private class SensorStatus {
        private Status currentStatus;
        private final List<Measurement> values = new ArrayList<>();

        public SensorStatus(Measurement measurement) {
            initStatus();
            set(measurement);
        }

        private void initStatus() {
            currentStatus = Status.OK;
            values.add(new Measurement(0, OffsetDateTime.now()));
            values.add(new Measurement(0, OffsetDateTime.now()));
            values.add(new Measurement(0, OffsetDateTime.now()));
        }

        Status current() {
            return currentStatus;
        }

        void set(Measurement measurement) {
            values.remove(0);
            values.add(measurement);

            if (currentStatus == Status.WARN) {
                if (allAbove2000()) {
                    currentStatus = Status.ALERT;
                    alerts.add(new Alert(new LinkedList<>(values)));
                } else if (allBellow2000()) {
                    currentStatus = Status.OK;
                }
            } else if (currentStatus == Status.OK) {
                if (measurement.co2() > 2000) {
                    currentStatus = Status.WARN;
                }
            } else {
                if (allBellow2000()) {
                    currentStatus = Status.OK;
                }
            }
        }

        private boolean allBellow2000() {
            return values.stream().allMatch(v -> v.co2() <= 2000);
        }

        private boolean allAbove2000() {
            return values.stream().allMatch(v -> v.co2() > 2000);
        }
    }
}
