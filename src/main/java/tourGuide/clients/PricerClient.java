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

import java.util.List;
import java.util.Set;


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

    public Integer getUserRewards(Set<Attraction> attractions) {

        ObjectMapper postMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = postMapper
                    .writeValueAsString(attractions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpGet request = new HttpGet("http://localhost:8083/calculateRewards?attractions=" + requestBody);
        request.addHeader(HttpHeaders.ACCEPT, "MediaType.APPLICATION_JSON_VALUE");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Integer rewardsPoint = objectMapper.readValue(response.toString(), Integer.class);
            return rewardsPoint;
        } catch (Exception e) {
            log.error("cannot send the get http request");
        }
        return null;
    }
}