package com.pachoyan;

import com.pachoyan.sensors.*;

import java.util.Collection;
import java.util.Optional;

import static spark.Spark.get;
import static spark.Spark.post;

public class Application {
    public static void main(String[] args) {
        final JsonMapper jsonMapper = new JsonMapper();
        final Sensors sensors = new InMemorySensors();

        post("/api/v1/sensors/:uuid/measurements", (req, res) -> {
            final Measurement measurement = jsonMapper.readJson(req.body(), Measurement.class);
            sensors.register(req.params("uuid"), measurement);
            res.status(202);
            return "";
        });

        get("/api/v1/sensors/:uuid", (req, res) -> {
            final Optional<Sensor.Status> sensor = sensors.find(req.params("uuid")).map(Sensor::status);
            if (sensor.isPresent())
                return sensor.get().name();

            res.status(404);
            return "";

        });

        get("/api/v1/sensors/:uuid/metrics", (req, res) -> {
            final Optional<SensorStatistics> statistics = sensors.find(req.params("uuid"))
                    .map(Sensor::statisticsLast30Days);
            if (statistics.isPresent())
                return statistics.get();

            res.status(404);
            return res;

        }, jsonMapper::toJson);

        get("/api/v1/sensors/:uuid/alerts", (req, res) -> {
            final Optional<Collection<Alert>> alerts =
                    sensors.find(req.params("uuid")).map(Sensor::alerts).map(Alerts::all);
            if (alerts.isPresent())
                return alerts.get();

            res.status(404);
            return res;
        }, jsonMapper::toJson);
    }

}
