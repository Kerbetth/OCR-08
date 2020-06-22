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
import tourGuide.clients.dto.UserService.UserPreferences;
import tourGuide.clients.dto.UserService.User;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;


@Slf4j
@Repository
public class UserClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public User getUser(String userName) {
        HttpGet request = new HttpGet("http://localhost:8081/getUser?userName=" + userName);
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(response.toString(), User.class);
            return user;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public List<UUID> getAllUsersUUID() {
        HttpGet request = new HttpGet("http://localhost:8081/getAllUsersID");
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<UUID> users = objectMapper.readValue(response.toString(), List.class);
            return users;
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