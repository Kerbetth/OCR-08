package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PricerClient pricerClient;

    @PostMapping("/setUserPreferences")
    public void setUserPreferences(@RequestParam String userId,
            @RequestBody SetUserPreferences userPreferences) {
        userService.setUserPreferences(userId, userPreferences);
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody CreateUser createUser) {
        userService.addUser(createUser);
    }

    @GetMapping("/getUserRewardsPoints")
    public int getCumulativePointsUserRewards(@RequestParam String userName) {
        return userService.getCumulateRewardPoints(userName);
    }

    @GetMapping("/getUserRewardSize")
    public int getUserRewardSize(@RequestParam String userId) {
        return userService.getUserRewardSize(userId);
    }

    @GetMapping("/getTripPricerTask")
    public TripPricerTask getTripPricerTask(@RequestParam String userName) {
        return userService.getTripPricerTask(userName);
    }

    /*********************
     * Controller for tests
     *********************/

    @PostMapping("/addUserAttractionLocation")
    public void addUserAttractionLocation(@RequestParam String userId, @RequestBody TrackerResponse trackerResponse) {
        userService.addUserLocation(userId, trackerResponse.visitedLocation);
        if (trackerResponse.attraction != null) {
            userService.addUserReward(userId, pricerClient.generateUserReward(trackerResponse));
        }
    }

}