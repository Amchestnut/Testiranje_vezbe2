package org.example.parametrized;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

public class EarthquakeParameterizedTests {

    private static double parseDoubleOrNaN(String s) {
        if (s == null)
            return Double.NaN;

        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("null"))
            return Double.NaN;

        return Double.parseDouble(s);
    }

    @ParameterizedTest(name = "[{index}] id={0} magnitude={1} place={2}")
    @CsvFileSource(resources = "/earthquakes.csv", numLinesToSkip = 1)
    void csvSample_validatesBasicSchema(String id, String magnitudeStr, String place, long epochMs, double lon, double lat, double depthKm) {
        double magnitude = parseDoubleOrNaN(magnitudeStr);

        // Basic checks
        assertNotNull(id);
        assertNotNull(place);
        assertTrue(lon >= -180 && lon <= 180);
        assertTrue(lat >= -90 && lat <= 90);
        assertTrue(depthKm >= -10 && depthKm <= 700); // crust to upper mantle (gornji omotac zemlje)

        // Magnitude must be logically valid
        assertTrue(Double.isNaN(magnitude) || (magnitude >= 0 && magnitude <= 12));

        // Epoch must be positive
        assertTrue(epochMs > 0);
    }
}

