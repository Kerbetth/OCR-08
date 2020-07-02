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
import tourGuide.clients.dto.pricerreward.Provider;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.VisitedLocation;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Repository
public class PricerClient extends SenderClient{

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public List<Provider> getTripDeals(String userName) {
        HttpGet request = new HttpGet("http://localhost:8083/getTripDeals");
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Provider> users = objectMapper.readValue(response.toString(), List.class);
            return users;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }

    public Integer getUserRewards(Set<UUID> attractions, UUID userId) {
        String uuids = attractions.toString();
        uuids = uuids.substring(1);
        uuids = uuids.substring(0,uuids.length()-1);
        uuids = uuids.replace(" ", "");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/calculateRewards?attractionsId="+uuids+"&userId="+userId.toString()))
                .build();

        HttpResponse<String> response = sendRequest(request);

        return Integer.parseInt(response.body());
    }

    public int getAttractionRewards(List<String> attractionName) {
        String uuids = attractionName.toString();
        uuids = uuids.substring(1);
        uuids = uuids.substring(0,uuids.length()-1);
        uuids = uuids.replace(" ", "");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/getAttractionRewards?attractionsName="+uuids))
                .build();

        HttpResponse<String> response = sendRequest(request);

        return Integer.parseInt(response.body());
    }
}