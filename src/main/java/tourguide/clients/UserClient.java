package tourguide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.UserReward;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Repository
public class UserClient extends SenderClient {

    public HttpResponse<String> setUserPreferences(SetUserPreferences pref) {
        String requestBody = null;
        try {
            requestBody = mapper
                    .writeValueAsString(pref);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/setUserPreferences"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return sendRequest(request);
    }

    public void addUser(CreateUser createUser) {
        String requestBody = null;
        try {
            requestBody = mapper
                    .writeValueAsString(createUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUser"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        sendRequest(request);
    }

    public void addUserLocation(String uuid, VisitedLocation visitedLocation) {
        String requestBody = null;
        try {
            requestBody = mapper
                    .writeValueAsString(visitedLocation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUserLocation?userId=" + uuid))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        sendRequest(request);
    }

    public void addUserReward(String userID, UserReward userReward) {
        String requestBody = null;
        try {
            requestBody = mapper
                    .writeValueAsString(userReward);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/addUserReward?userId=" + userID))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        sendRequest(request);
    }

    public UUID getUserId(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserId?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        UUID user = null;
        try {
            user = mapper.readValue(response.body(), UUID.class);
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

        Location location = null;
        try {
            location = mapper.readValue(response.body(), Location.class);
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

        List<String> users = new ArrayList<>();
        try {
            users = mapper.readValue(response.body(), List.class);
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

        List<VisitedLocation> visitedLocations = null;
        try {
            visitedLocations = mapper.readValue(response.body(), List.class);
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

        TripPricerTask tripPricerTask = null;
        try {
            tripPricerTask = mapper.readValue(response.body(), TripPricerTask.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getTripPricerTask json response");
        }
        return tripPricerTask;
    }

    public List<String> getVisitedAttractionId(String uuid) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getAttractionIds?userId=" + uuid))
                .build();

        HttpResponse<String> response = sendRequest(request);
        List<String> attractionsId = new ArrayList<>();
        try {
            attractionsId = mapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllUsersName json response");
        }
        return attractionsId;
    }

    public int getCumulativePointsUserRewards(String userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getCumulateRewardPoints?userId=" + userId))
                .build();

        HttpResponse<String> response = sendRequest(request);
        return Integer.parseInt(response.body());
    }

    public int getUserRewardSize(String userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserRewardSize?userId=" + userId))
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

        List<String> userName = new ArrayList<>();
        try {
            userName = mapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getAllUsersName json response");
        }
        System.out.println("test");
        return userName;
    }



}