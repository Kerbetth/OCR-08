package tourGuide.clients;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Slf4j
@Repository
public class SenderClient {
    protected HttpResponse<String> sendRequest(HttpRequest httpRequest){
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newBuilder().build().send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}