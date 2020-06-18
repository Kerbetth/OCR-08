package tourGuide.clients;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Repository;
import tourGuide.domain.User;
import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;


@Slf4j
@Repository
public class TrackerClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public Location getLocation(String userName) {
        HttpGet request = new HttpGet(
                "http://localhost:8082/getLocation?userName="
                + userName);
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

    public FiveNearestAttractions get5NearestAttraction(Location location) {
        HttpGet request = new HttpGet(
                "http://localhost:8082/getFiveNearbyAttractions?longitude="
                        + location.longitude
                        + "&latitude="
                        + location.latitude);
        request.addHeader(org.springframework.http.HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            FiveNearestAttractions fiveNearestAttractions = objectMapper.readValue(response.toString(), FiveNearestAttractions.class);
            return fiveNearestAttractions;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

}