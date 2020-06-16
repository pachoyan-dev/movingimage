package com.pachoyan.sensors;

import java.util.Collection;

public interface Alerts {
    Collection<Alert> all();

    void add(Alert alert);
}
