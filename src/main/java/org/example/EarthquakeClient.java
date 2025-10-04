package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeClient {
    private static final String FEED_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Earthquake> fetchLastHour() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(FEED_URL))
                .timeout(Duration.ofSeconds(20))
                .GET().build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            throw new IllegalStateException("USGS feed error: " + res.statusCode());
        }

        JsonNode root = mapper.readTree(res.body());
        JsonNode features = root.get("features");

        List<Earthquake> earthquakes = new ArrayList<>();

        if (features != null && features.isArray()) {
            // Mapiramo svaki FEATURE u EARTHQUAKE (jedan f je jedan earthquake)
            for (JsonNode feature : features) {
                String earthquakeId = feature.get("id").asText();

                // Ako mag ponekad bude null, uzmi "null" kao default
                JsonNode propertiesNode = feature.get("properties");
                double magnitude = propertiesNode.path("mag").asDouble(Double.NaN);
                String placeDescription = propertiesNode.get("place").asText(null);
                long eventTimeEpochMs = propertiesNode.get("time").asLong();
                var coordinatesArrayNode = (ArrayNode) feature.get("geometry").get("coordinates");
                double longitude = coordinatesArrayNode.get(0).asDouble();
                double latitude  = coordinatesArrayNode.get(1).asDouble();
                double depthKilometers = coordinatesArrayNode.get(2).asDouble();

                earthquakes.add(new Earthquake(earthquakeId, magnitude, placeDescription, eventTimeEpochMs, longitude, latitude, depthKilometers));
            }
        }
        return earthquakes;
    }
}

