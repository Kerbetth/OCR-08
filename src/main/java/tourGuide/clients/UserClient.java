package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Repository;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.UserPreferences;
import tourGuide.clients.dto.userservice.User;


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
public class UserClient extends SenderClient{

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public User getUser(String userName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUser?userName=" + userName))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try {
            user = objectMapper.readValue(response.body(), User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getUser json response");
        }
        return user;
    }

    public List<UUID> getAllUsersUUID() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getAllUsersID"))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<UUID> users = new ArrayList<>();
        try {
            users = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the getUser json response");
        }
        return users;
    }

    public Location getUserLocation(String userName) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/getUserLocation?userName=" + userName))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

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



    public List<VisitedLocation> getAllVisitedLocations(String userName) {
        HttpGet request = new HttpGet("http://localhost:8081/getAllVisitedLocations?userName=" + userName);
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<VisitedLocation> visitedLocations = objectMapper.readValue(response.toString(), List.class);
            return visitedLocations;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public HttpResponse<String> setUserPreferences(UserPreferences userPreferences) {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(userPreferences);
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


}