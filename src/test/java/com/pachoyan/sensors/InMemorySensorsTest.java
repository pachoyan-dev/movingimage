package com.pachoyan.sensors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

class InMemorySensorsTest {
    private final Map<String, Sensor> delegate = new HashMap<>();
    private final InMemorySensors sensors = new InMemorySensors(delegate);

    @Test
    void doesNotFindNoRegisteredSensor() {
        assertThat(sensors.find("any-uuid")).isEmpty();
    }

    @Nested
    class WhenRegisterMeasurement {
        @BeforeEach
        void setUp() {
            sensors.register(id(), new Measurement(2500, time()));
        }

        @Test
        void containsMeasurement() {
            assertThat(delegate).containsEntry(id(), new InMemorySensor(measurement()));
        }

        private Measurement measurement() {
            return new Measurement(2500, time());
        }

        private OffsetDateTime time() {
            return OffsetDateTime.parse("2019-02-01T10:20:45Z");
        }

        private String id() {
            return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
        }
    }

    @Nested
    class FindsRegisteredMeasurement {

        @BeforeEach
        void setUp() {
            sensors.register(id(), measurement());
        }

        @Test
        void findsMeasurement() {
            assertThat(sensors.find(id())).contains(new InMemorySensor(measurement()));
        }

        private Measurement measurement() {
            return new Measurement(2500, time());
        }

        private OffsetDateTime time() {
            return OffsetDateTime.parse("2019-02-01T10:20:45Z");
        }

        private String id() {
            return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
        }
    }

    @Nested
    class StatisticsForLast30Days {

        @Nested
        class StatisticsForPast30DaysValues {
            @BeforeEach
            void setUp() {
                sensors.register(id(), measurement1());
                sensors.register(id(), measurement2());
            }

            @Test
            void avgIs100() {
                assertThat(sensors.find(id()).orElseThrow(IllegalStateException::new).statisticsLast30Days().avg())
                        .isEqualTo(2000);
            }

            @Test
            void maxIs2500() {
                assertThat(sensors.find(id()).orElseThrow(IllegalStateException::new).statisticsLast30Days().max())
                        .isEqualTo(2500);
            }

            private Measurement measurement1() {
                return new Measurement(1500, OffsetDateTime.now());
            }


            private Measurement measurement2() {
                return new Measurement(2500, OffsetDateTime.now().minusDays(2));
            }


            private String id() {
                return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
            }
        }

        @Nested
        class SkipsTimesPast30Days {
            @BeforeEach
            void setUp() {
                sensors.register(id(), last30DaysMeasurement());
                sensors.register(id(), pastLast30DaysMeasurement());

            }

            @Test
            void avgIs2500() {
                assertThat(sensors.find(id()).orElseThrow(IllegalStateException::new).statisticsLast30Days().avg())
                        .isEqualTo(2500);
            }

            @Test
            void maxIs2500() {
                assertThat(sensors.find(id()).orElseThrow(IllegalStateException::new).statisticsLast30Days().max()).isEqualTo(2500);
            }

            private Measurement last30DaysMeasurement() {
                return new Measurement(2500, OffsetDateTime.now());
            }


            private Measurement pastLast30DaysMeasurement() {
                return new Measurement(3000, OffsetDateTime.now().minusDays(31));
            }


            private String id() {
                return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
            }
        }

        @Nested
        class WhenMultipleSensors {

            @BeforeEach
            void setUp() {
                sensors.register(id1(), measurement1());
                sensors.register(id2(), measurement2());

            }

            @Test
            void avg1Is2500() {
                assertThat(sensors.find(id1()).orElseThrow(IllegalStateException::new).statisticsLast30Days().avg())
                        .isEqualTo(2500);
            }

            @Test
            void max1Is2500() {
                assertThat(sensors.find(id1()).orElseThrow(IllegalStateException::new).statisticsLast30Days().max())
                        .isEqualTo(2500);
            }

            @Test
            void avg2Is3000() {
                assertThat(sensors.find(id2()).orElseThrow(IllegalStateException::new).statisticsLast30Days().avg())
                        .isEqualTo(3000);
            }

            @Test
            void max2Is3000() {
                assertThat(sensors.find(id2()).orElseThrow(IllegalStateException::new).statisticsLast30Days().max())
                        .isEqualTo(3000);
            }

            private Measurement measurement1() {
                return new Measurement(2500, OffsetDateTime.now());
            }


            private Measurement measurement2() {
                return new Measurement(3000, OffsetDateTime.now());
            }

            private String id1() {
                return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
            }

            private String id2() {
                return "6979cc34-c288-4c7e-acff-49946d404425";
            }
        }
    }

    @Nested
    class SensorStatus {

        private Sensor sensor() {
            return sensors.find(id()).orElseThrow(IllegalStateException::new);
        }

        @Nested
        class WhenMeasurementIsOk {
            @BeforeEach
            void setUp() {
                sensors.register(id(), new Measurement(0, OffsetDateTime.now()));
                assumeThat(sensor().status()).isEqualTo(Sensor.Status.OK);
            }

            @Nested
            class WhenCo2Above2000Collected {
                @BeforeEach
                void setUp() {
                    sensors.register(id(), co2Above2000());
                }

                @Test
                void statusIsWarn() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.WARN);
                }
            }

            @Nested
            class WhenCo2BelowCo2Collected {
                @BeforeEach
                void setUp() {
                    sensors.register(id(), co2Below2000());
                }

                @Test
                void statusIsOK() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.OK);
                }
            }
        }


        @Nested
        class WhenMeasurementIsWarn {
            @BeforeEach
            void setUp() {
                sensors.register(id(), co2Above2000());
                sensors.register(id(), co2Below2000());
                assumeThat(sensor().status()).isEqualTo(Sensor.Status.WARN);
            }

            @Nested
            class WhenThreeCo2Above2000InARowCollected {
                final Measurement measurement1 = co2Above2000();
                final Measurement measurement2 = co2Above2000();
                final Measurement measurement3 = co2Above2000();

                @BeforeEach
                void setUp() {
                    sensors.register(id(), measurement1);
                    sensors.register(id(), measurement2);
                    sensors.register(id(), measurement3);
                }

                @Test
                void statusIsAlert() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.ALERT);
                }

                @Test
                void alarmCreated() {
                    assertThat(sensor().alerts().all()).containsExactly(
                            new Alert(new LinkedList<>(asList(measurement1, measurement2, measurement3)))
                    );
                }
            }


            @Nested
            class WhenThreeCo2Below2000InARowCollected {
                final Measurement measurement1 = co2Below2000();
                final Measurement measurement2 = co2Below2000();
                final Measurement measurement3 = co2Below2000();

                @BeforeEach
                void setUp() {
                    sensors.register(id(), measurement1);
                    sensors.register(id(), measurement2);
                    sensors.register(id(), measurement3);
                }

                @Test
                void statusIsOk() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.OK);
                }
            }

        }

        @Nested
        class WhenMeasurementIsAlert {
            @BeforeEach
            void setUp() {
                sensors.register(id(), co2Above2000());
                sensors.register(id(), co2Above2000());
                sensors.register(id(), co2Above2000());
                assumeThat(sensor().status()).isEqualTo(Sensor.Status.ALERT);
            }

            @Nested
            class WhenThreeCo2Below2000InARowCollected {
                @BeforeEach
                void setUp() {
                    sensors.register(id(), co2Below2000());
                    sensors.register(id(), co2Below2000());
                    sensors.register(id(), co2Below2000());
                }

                @Test
                void statusIsOk() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.OK);
                }
            }

            @Nested
            class WhenCo2Above200Collected {
                @BeforeEach
                void setUp() {
                    sensors.register(id(), co2Above2000());
                }

                @Test
                void statusIsStillAlert() {
                    assertThat(sensor().status()).isEqualTo(Sensor.Status.ALERT);
                }
            }
        }

        private Measurement co2Above2000() {
            return new Measurement(3000, OffsetDateTime.now());
        }


        private Measurement co2Below2000() {
            return new Measurement(1000, OffsetDateTime.now());
        }

        private String id() {
            return "4ef7cb99-7c7f-409e-a98b-2abdb3f6f1cc";
        }

    }
}