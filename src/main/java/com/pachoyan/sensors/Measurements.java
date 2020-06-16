package com.pachoyan.sensors;

import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Measurements {

    LinkedList<Measurement> all();

    void add(Measurement measurement);

    default Stream<Measurement> stream() {
        return all().stream();
    }

    Optional<Measurement> last();

    default IntSummaryStatistics statistics() {
        return stream().mapToInt(Measurement::co2).summaryStatistics();
    }

    default IntSummaryStatistics statisticsFor(Predicate<Measurement> filter) {
        return stream()
                .filter(filter)
                .mapToInt(Measurement::co2)
                .summaryStatistics();
    }
}
