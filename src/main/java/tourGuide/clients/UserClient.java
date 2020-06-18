package tourGuide.clients;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Repository;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.domain.User;


import java.util.List;
import java.util.Map;
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

    public List<User> getAllUsers() {
        HttpGet request = new HttpGet("http://localhost:8081/getAllUsers");
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> users = objectMapper.readValue(response.toString(), List.class);
            return users;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public Map<UUID, Location> getAllCurrentLocations() {
        HttpGet request = new HttpGet("http://localhost:8081/getAllCurrentLocations");
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<UUID, Location> users = objectMapper.readValue(response.toString(), Map.class);
            return users;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

}