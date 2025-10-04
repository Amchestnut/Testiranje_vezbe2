package org.example.dynamic;

import org.example.Earthquake;
import org.example.EarthquakeClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class EarthquakeDynamicTests {

    @TestFactory
    @DisplayName("USGS live: 1 dynamic test per 1 earthquake feature")
    Stream<DynamicTest> liveFeedDynamicTests() throws Exception {
        EarthquakeClient client = new EarthquakeClient();
        List<Earthquake> earthquakes = client.fetchLastHour();

        if (earthquakes.isEmpty()) {
            return Stream.of(dynamicTest("feed empty -> nothing to validate", () -> assertTrue(true)));
        }

        //  Check if all ids are unique
        Set<String> ids = earthquakes.stream().map(Earthquake::getId).collect(Collectors.toSet());
        assertEquals(earthquakes.size(), ids.size(), "IDs should be unique");

        // Now generate per-item dynamic tests.
        return earthquakes.stream().map(earthquake ->
                dynamicTest("EQ " + (earthquake.getId() == null ? "(no-id)" : earthquake.getId()), () -> {
                    // check if ID is present
                    assertNotNull(earthquake.getId(), "id");

                    // Coordinates
                    assertTrue(earthquake.getLongitude() >= -180 && earthquake.getLongitude() <= 180, "lon bounds");
                    assertTrue(earthquake.getLatitude() >= -90 && earthquake.getLatitude() <= 90, "lat bounds");
                    assertFalse(Double.isNaN(earthquake.getDepthKm()), "depth should exist");

                    // Magnitude range (USGS may publish null for some events)
                    assertTrue(Double.isNaN(earthquake.getMagnitude()) || (earthquake.getMagnitude() >= 0 && earthquake.getMagnitude() <= 12),
                            "magnitude in plausible range or none");

                    // Time within last 65 minutes
                    long ageMinutes = Duration.between(Instant.ofEpochMilli(earthquake.getTimeEpochMs()), Instant.now()).toMinutes();
                    assertTrue(ageMinutes >= 0 && ageMinutes <= 65, "time within last 65 minutes");
                })
        );
    }
}

