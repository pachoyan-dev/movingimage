package com.pachoyan.sensors;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class InMemoryAlerts implements Alerts {
    private final LinkedList<Alert> alerts;

    public InMemoryAlerts() {
        this(new LinkedList<>());
    }

    public InMemoryAlerts(LinkedList<Alert> alerts) {
        this.alerts = alerts;
    }

    @Override
    public Collection<Alert> all() {
        return alerts;
    }

    @Override
    public void add(Alert alert) {
        alerts.add(alert);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryAlerts that = (InMemoryAlerts) o;
        return Objects.equals(alerts, that.alerts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alerts);
    }
}
