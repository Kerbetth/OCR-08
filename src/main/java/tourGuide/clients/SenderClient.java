package tourGuide.clients;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Repository
public class SenderClient {
    protected HttpResponse<String> sendRequest(HttpRequest httpRequest){
        HttpResponse<String> response = null;
        HttpClient client = HttpClient.newHttpClient();
        try {
            response = client.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}