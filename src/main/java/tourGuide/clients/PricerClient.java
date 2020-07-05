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
import tourGuide.clients.dto.pricerreward.TripPricerTask;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.UserReward;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Repository
public class PricerClient extends SenderClient{

    public List<Provider> getTripDeals(TripPricerTask tripPricerTask, int rewards) {

        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(tripPricerTask);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8083/getTripDeals?rewards=" + rewards))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Provider> attractions = null;
        try {
            attractions = objectMapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the List<Provider> json response");
        }
        return attractions;
    }

    public int getUserRewardsPoints(List<UserReward> userRewards) {

        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(userRewards);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/getCumulativeUserRewardPoints"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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
                .uri(URI.create("http://localhost:8083/getCumulativeAttractionRewardPoints?attractionsName="+uuids))
                .build();

        HttpResponse<String> response = sendRequest(request);

        return Integer.parseInt(response.body());
    }

    public UserReward generateUserReward(Attraction attraction, VisitedLocation visitedLocation) {
        ObjectMapper postMapper = new ObjectMapper();
        String attractionBody = null;
        try {
            attractionBody = postMapper
                    .writeValueAsString(attraction);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String visitedLocationBody = null;
        try {
            visitedLocationBody = postMapper
                    .writeValueAsString(visitedLocation);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8083/generateUserReward"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(attractionBody))
                .POST(HttpRequest.BodyPublishers.ofString(visitedLocationBody))
                .build();

        HttpResponse<String> response = sendRequest(request);

        ObjectMapper objectMapper = new ObjectMapper();
        UserReward userReward = null;
        try {
            userReward = objectMapper.readValue(response.body(), UserReward.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the List<Provider> json response");
        }
        return userReward;
    }
}