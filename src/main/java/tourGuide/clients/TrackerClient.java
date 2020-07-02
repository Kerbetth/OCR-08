package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Repository;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


@Slf4j
@Repository
public class TrackerClient extends SenderClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public Location getLocation(UUID userId) {
        HttpGet request = new HttpGet(
                "http://localhost:8082/getLocation?userId="
                        + userId);
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Location location = objectMapper.readValue(response.toString(), Location.class);
            return location;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public Map<UUID, Location> getAllCurrentLocations(List<UUID> listUserID) {
        String uuids = listUserID.toString();
        uuids = uuids.substring(1);
        uuids = uuids.substring(0,uuids.length()-1);
        uuids = uuids.replace(" ", "");

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getAllCurrentLocations?userId=" + uuids))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<UUID, Location> uuidLocationMap = null;
        try {
            uuidLocationMap = objectMapper.readValue(response.body(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllCurrentLocations json response");
        }
        return uuidLocationMap;
    }

    public Set<UUID> getAllVisitedAttractionId(List<VisitedLocation> visitedLocations) {
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(visitedLocations);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getAllVisitedAttractions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        Set<UUID> attractions = null;
        try {
            attractions = objectMapper.readValue(response.body(), Set.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the get5NearestAttraction json response");
        }
        return attractions;
    }

    public FiveNearestAttractions get5NearestAttraction(Location location) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonLocation = null;
        try {
            jsonLocation = mapper.writeValueAsString(location);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/get5NearestAttractions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonLocation))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        FiveNearestAttractions fiveNearestAttractions = null;
        try {
            fiveNearestAttractions = objectMapper.readValue(response.body(), FiveNearestAttractions.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the get5NearestAttraction json response");
        }
        return fiveNearestAttractions;
    }


}