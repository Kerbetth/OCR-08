package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tourGuide.clients.dto.CreateUser;
import tourGuide.clients.dto.SetUserPreferences;
import tourGuide.clients.dto.pricerreward.TripPricerTask;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.UserReward;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Repository
public class UserClient extends SenderClient {

    public HttpResponse<String> setUserPreferences(SetUserPreferences pref) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(pref);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/setUserPreferences"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void addUser(CreateUser createUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(createUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUser"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    public void addUserLocation(String uuid, VisitedLocation trackUserLocation) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(trackUserLocation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUserLocation?userId=" + uuid))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    public void addUserReward(String userID, UserReward userReward) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(userReward);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUserReward?userId=" + userID))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    public UUID getUserId(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserId?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        UUID user = null;
        try {
            user = objectMapper.readValue(response.body(), UUID.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getUserId json response");
        }
        return user;
    }

    public Location getUserLocation(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserLocation?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        Location location = null;
        try {
            location = objectMapper.readValue(response.body(), Location.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot send the get http request");
        }
        return location;
    }

    public List<String> getAllUsersUUID() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getAllUsersID"))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> users = new ArrayList<>();
        try {
            users = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getUser json response");
        }
        return users;
    }

    public List<VisitedLocation> getAllVisitedLocations(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getAllVisitedLocations?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<VisitedLocation> visitedLocations = null;
        try {
            visitedLocations = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot send the get http request");
        }
        return visitedLocations;
    }

    public TripPricerTask getTripPricerTask(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getTripPricerTask?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        TripPricerTask tripPricerTask = null;
        try {
            tripPricerTask = objectMapper.readValue(response.body(), TripPricerTask.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getTripPricerTask json response");
        }
        return tripPricerTask;
    }

    public List<String> getVisitedAttractionId(String uuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserRewardsId?userId=" + uuid))
                .build();

        HttpResponse<String> response = sendRequest(request);
        List<String> attractionsId = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            attractionsId = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllUsersName json response");
        }
        return attractionsId;
    }

    public int getCumulativePointsUserRewards(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserRewards?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);
        return Integer.parseInt(response.body());
    }

/*
    public UserPreferences getUserPreferences(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserPreferences?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);
        UserPreferences userPreferences = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            userPreferences = objectMapper.readValue(response.body(), UserPreferences.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllUsersName json response");
        }
        return userPreferences;
    }*/



    /**
     * test methods
     */

    public List<String> getAllUsersName() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getAllUsersName"))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> userName = new ArrayList<>();
        try {
            userName = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllUsersName json response");
        }
        System.out.println("test");
        return userName;
    }



}