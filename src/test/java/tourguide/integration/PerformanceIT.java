package tourguide.integration;


import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.controller.TrackerController;
import tourguide.controller.UserController;
import tourguide.service.UserService;

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


@SpringBootTest(classes = tourguide.Application.class)
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
    UserService userService;
    @Autowired
    UserController userController;
    @Autowired
    TrackerController trackerController;

    List<String> allUsers;

    @BeforeEach
    void setup() {
        allUsers = userService.getAllUsersID();
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


    @Test
    public void highVolumeGetRewards() {
        // ARRANGE
        StopWatch stopWatch = new StopWatch();
        Attraction attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
        TrackerResponse trackerResponse = new TrackerResponse(new VisitedLocation(UUID.randomUUID(), attraction, new Date()),attraction);
        // ACT
        stopWatch.start();
        allUsers.forEach(u -> userController.addUserAttractionLocation(u, new TrackerResponse(new VisitedLocation(UUID.randomUUID(), attraction, new Date()),attraction)));
        allUsers.forEach(u -> assertTrue(userService.getUserRewardSize(u) > 0));
        stopWatch.stop();

        //ASSERT
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
