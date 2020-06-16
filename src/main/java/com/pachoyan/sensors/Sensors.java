package com.pachoyan.sensors;

import java.util.Optional;

public interface Sensors {
    Optional<Sensor> find(String id);

    void register(String uuid, Measurement measurement);
}
