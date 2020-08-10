package tourguide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.pricerreward.Provider;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.clients.dto.userservice.UserReward;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Slf4j
@Repository
public class PricerClient extends SenderClient{

    public UserReward generateUserReward(TrackerResponse trackerResponse) {
        String attractionBody = null;
        try {
            attractionBody = mapper
                    .writeValueAsString(trackerResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8083/generateUserReward"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(attractionBody))
                .build();

        HttpResponse<String> response = sendRequest(request);

        UserReward userReward = null;
        try {
            userReward = mapper.readValue(response.body(), UserReward.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the List<Provider> json response");
        }
        return userReward;
    }

    public int getAttractionRewardsPoint(List<String> attractionName) {
        String attractionNameList = attractionName.toString();
        attractionNameList = attractionNameList.substring(1);
        attractionNameList = attractionNameList.substring(0,attractionNameList.length()-1);
        attractionNameList = attractionNameList.replace(" ", "");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8083/getCumulativeAttractionRewardPoints?attractionsName="+attractionNameList))
                .build();

        HttpResponse<String> response = sendRequest(request);
        return Integer.parseInt(response.body());
    }

    public List<Provider> getTripDeals(TripPricerTask tripPricerTask, int rewards) {

        String requestBody = null;
        try {
            requestBody = mapper
                    .writeValueAsString(tripPricerTask);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        java.net.http.HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8083/getTripDeals?rewards="
                                +rewards))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = sendRequest(request);
        List<Provider> attractions = null;
        try {
            attractions = mapper.readValue(response.body(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("cannot read the List<Provider> json response");
        }
        return attractions;
    }


}