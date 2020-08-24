package tourguide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.clients.dto.trackerservice.FiveNearestAttractions;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.UserReward;

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


    public TrackerResponse trackUserLocation(String userId) {
        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/trackUserLocation?userId=" + userId))
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
        String jsonLocation = null;
        try {
            jsonLocation = mapper.writeValueAsString(listUserID);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8082/getCurrentLocationOfAllUsers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonLocation))
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
}