package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.clients.PricerClient;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.CreateUser;
import tourGuide.clients.dto.SetUserPreferences;
import tourGuide.clients.dto.TrackerResponse;
import tourGuide.clients.dto.pricerreward.TripPricerTask;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.clients.dto.userservice.UserPreferences;

import java.net.http.HttpResponse;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserClient userClient;
    @Autowired
    PricerClient pricerClient;

    @PostMapping("/setUserPreferences")
    public HttpResponse<String> setUserPreferences(
            @RequestBody SetUserPreferences userPreferences) {
        return userClient.setUserPreferences(userPreferences);
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody CreateUser createUser) {
        userClient.addUser(createUser);
    }

    @GetMapping("/getUserLocation")
    public Location getUserLocation(@RequestParam String userId) {
        return userClient.getUserLocation(userId);
    }

    @GetMapping("/getUserRewardsPoints")
    public int getCumulativePointsUserRewards(@RequestParam String userName) {
        return userClient.getCumulativePointsUserRewards(userName);
    }

    @GetMapping("/getUserRewardSize")
    public int getUserRewardSize(@RequestParam String userId) {
        return userClient.getUserRewardSize(userId);
    }

    @GetMapping("/getTripPricerTask")
    public TripPricerTask getTripPricerTask(@RequestParam String userName) {
        return userClient.getTripPricerTask(userName);
    }


    /*********************
     * Controller for tests
     *********************/

    @PostMapping("/addUserLocation")
    public void addUserAttractionLocation(@RequestParam String userId, @RequestBody TrackerResponse trackerResponse) {
        userClient.addUserLocation(userId, trackerResponse.visitedLocation);
        if (trackerResponse.attraction != null) {
            userClient.addUserReward(userId, pricerClient.generateUserReward(trackerResponse));
        }
    }

}