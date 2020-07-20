package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourguide.clients.PricerClient;
import tourguide.clients.UserClient;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;

import java.net.http.HttpResponse;

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