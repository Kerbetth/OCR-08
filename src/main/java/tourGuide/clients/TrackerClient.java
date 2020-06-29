package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
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

import java.net.URI;
import java.util.*;


@Slf4j
@Repository
public class TrackerClient {

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
        HttpGet request = new HttpGet("http://localhost:8081/getAllCurrentLocations?userId="+listUserID.toString());
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

    public Set<Attraction> getAllVisitedAttraction(List<VisitedLocation> visitedLocations) {
        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(visitedLocations);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpUriRequest request = new HttpGet("http://localhost:8081/getAllVisitedAttraction?userId="+requestBody);
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper getMapper = new ObjectMapper();
            Set<Attraction> users = getMapper.readValue(response.toString(), Set.class);
            return users;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public FiveNearestAttractions get5NearestAttraction(Location location) {
        HttpGet request = new HttpGet(
                "http://localhost:8082/get5NearestAttractions?longitude="
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