package com.pachoyan.sensors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemorySensors implements Sensors {
    private final Map<String, Sensor> sensors;

    public InMemorySensors() {
        this(new HashMap<>());
    }

    InMemorySensors(Map<String, Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public Optional<Sensor> find(String id) {
        return Optional.ofNullable(sensors.get(id));
    }

    @Override
    public void register(String uuid, Measurement measurement) {
        if (sensors.containsKey(uuid)) {
            sensors.get(uuid).register(measurement);
        } else {
            sensors.put(uuid, new InMemorySensor(measurement));
        }
    }
}
