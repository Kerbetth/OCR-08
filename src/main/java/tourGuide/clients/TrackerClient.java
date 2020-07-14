package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tourGuide.clients.dto.TrackerResponse;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.UserReward;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Repository
public class TrackerClient extends SenderClient {


    public TrackerResponse trackUserLocation(String userId, List<String> attractionId) {
        String jsonAttractionId = "[]";
        if(!attractionId.isEmpty()) {
            try {
                jsonAttractionId = mapper.writeValueAsString(attractionId);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/trackUserLocation?userId=" + userId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonAttractionId))
                .build();

        HttpResponse<String> response = sendRequest(request);

        TrackerResponse trackerResponse = null;
        try {
            trackerResponse = mapper.readValue(response.body(), TrackerResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the trackUserLocation json response");
        }
        return trackerResponse;
    }

    public FiveNearestAttractions get5NearestAttraction(Location location) {
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

        FiveNearestAttractions fiveNearestAttractions = null;
        try {
            fiveNearestAttractions = mapper.readValue(response.body(), FiveNearestAttractions.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the get5NearestAttraction json response");
        }
        return fiveNearestAttractions;
    }

    public Map<String, Location> getCurrentLocationOfAllUsers(List<String> listUserID) {
        String uuids = listUserID.toString();
        uuids = uuids.substring(1);
        uuids = uuids.substring(0, uuids.length() - 1);
        uuids = uuids.replace(" ", "");

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getCurrentLocationOfAllUsers?userId=" + uuids))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = sendRequest(request);

        Map<String, Location> uuidLocationMap = null;
        try {
            uuidLocationMap = mapper.readValue(response.body(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllCurrentLocations json response");
        }
        return uuidLocationMap;
    }

    public Set<UUID> getAllVisitedAttraction(List<VisitedLocation> visitedLocations) {
        String requestBody = null;
        try {
            requestBody = mapper
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

        Set<UUID> attractions = null;
        try {
            attractions = mapper.readValue(response.body(), Set.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the get5NearestAttraction json response");
        }
        return attractions;
    }
/*
    public List<Attraction> getAllAttraction() {
        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getAllAttraction"))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Attraction> attractions = null;
        try {
            attractions = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllAttraction json response");
        }
        return attractions;
    }*/

    public Attraction getNewVisitedAttraction(Location location,
                                              List<UserReward> userRewards) {
        String jsonReward = null;
        try {
            jsonReward = mapper.writeValueAsString(userRewards);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getNewVisitedAttraction?longitude=" + location.longitude + "&latitude=" + location.latitude))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonReward))
                .build();

        HttpResponse<String> response = sendRequest(request);

        Attraction attraction = null;
        if (!response.body().isEmpty()) {
            try {
                attraction = mapper.readValue(response.body(), Attraction.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                log.error("cannot read the get5NearestAttraction json response");
            }
            return attraction;
        }
        return null;
    }
}