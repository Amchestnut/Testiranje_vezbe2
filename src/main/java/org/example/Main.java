package org.example;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Main klasa, koja ispisuje sve zemljotrese koji su se desili zadnjih 60 min.

public class Main {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.systemDefault());

    public static void main(String[] args) throws Exception {
        EarthquakeClient client = new EarthquakeClient();
        List<Earthquake> earthquakes = client.fetchLastHour();

        System.out.println("Fetched " + earthquakes.size() + " events from USGS (last hour).");

        earthquakes.stream().limit(20).forEach(eq -> {
            String when = ISO.format(Instant.ofEpochMilli(eq.getTimeEpochMs()));
            System.out.printf(
                    "ID=%s | M=%.2f | %s | lat=%.4f lon=%.4f depth=%.1fkm | time=%s%n",
                    eq.getId(),
                    eq.getMagnitude(),
                    eq.getPlace(),          // mesto gde se desio zemljotres
                    eq.getLatitude(),
                    eq.getLongitude(),
                    eq.getDepthKm(),
                    when
            );
        });
    }
}

