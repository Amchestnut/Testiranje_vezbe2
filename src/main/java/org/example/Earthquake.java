package org.example;

public class Earthquake {
    private final String id;
    private final double magnitude; // can be NaN if missing
    private final String place;
    private final long timeEpochMs; // UNIX epoch millis
    private final double longitude;  // geometry.coordinates[0]
    private final double latitude;   // geometry.coordinates[1]
    private final double depthKm;    // geometry.coordinates[2]

    public Earthquake(String id, double magnitude, String place, long timeEpochMs,
                      double longitude, double latitude, double depthKm) {
        this.id = id;
        this.magnitude = magnitude;
        this.place = place;
        this.timeEpochMs = timeEpochMs;
        this.longitude = longitude;
        this.latitude = latitude;
        this.depthKm = depthKm;
    }

    // Getters
    public String getId() { return id; }
    public double getMagnitude() { return magnitude; }
    public String getPlace() { return place; }
    public long getTimeEpochMs() { return timeEpochMs; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public double getDepthKm() { return depthKm; }
}

