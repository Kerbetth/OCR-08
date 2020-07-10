package integration;


import okhttp3.Request;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.Application;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.controller.PricerController;
import tourGuide.controller.TrackerController;
import tourGuide.controller.UserController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


@SpringBootTest(classes = Application.class)
public class PerformanceIT {

    /**
     * A note on performance improvements:
     * <p>
     * The number of users generated for the high volume tests can be easily adjusted via this method:
     * <p>
     * InternalTestHelper.setInternalUserNumber(100000);
     * <p>
     * <p>
     * These tests can be modified to suit new solutions, just as long as the performance metrics
     * at the end of the tests remains consistent.
     * <p>
     * These are performance metrics that we are trying to hit:
     * <p>
     * highVolumeTrackLocation: 100,000 users within 15 minutes:
     * assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     * <p>
     * highVolumeGetRewards: 100,000 users within 20 minutes:
     * assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     **/

    @Autowired
    UserClient userClient;
    @Autowired
    UserController userController;
    @Autowired
    TrackerController trackerController;
    @Autowired
    PricerController pricerController;

    int defineInternalUserNumber;

    List<String> allUsers;

    @BeforeEach
    void setup() {
        HttpClient client = HttpClient.newHttpClient();
        defineInternalUserNumber = 50000;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/setInternalTestUser?number=" + defineInternalUserNumber))
                .build();
        try {
            client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allUsers = userClient.getAllUsersUUID();
    }

    @Test
    public void highVolumeTrackLocation() {
        // ARRANGE
        StopWatch stopWatch = new StopWatch();

        // ACT
        stopWatch.start();
        for (String uuid : allUsers) {
            trackerController.trackUserLocation(uuid);
        }
        stopWatch.stop();

        // ASSERT
        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

/*
    @Test
    public void highVolumeGetRewards() {
        // ARRANGE
        StopWatch stopWatch = new StopWatch();

        // ACT
        stopWatch.start();
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
        allUsers.forEach(u -> userController.addUserLocation(u, new VisitedLocation(UUID.randomUUID(), attraction, new Date())));
        allUsers.forEach(u -> userController.getUserRewardsPoints(u));
        for (UUID user : allUsers) {
            assertTrue(userClient.getCumulativePointsUserRewards(user) > 0);
        }
        stopWatch.stop();

        //ASSERT
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }
*/
}
