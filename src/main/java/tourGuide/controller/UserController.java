package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.CreateUser;
import tourGuide.clients.dto.SetUserPreferences;
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

    @PostMapping("/setUserPreferences")
    public HttpResponse<String> setUserPreferences(
            @RequestBody SetUserPreferences userPreferences) {
        return userClient.setUserPreferences(userPreferences);
    }

    @PostMapping("/addUserLocation")
    public void addUserLocation(@RequestParam String userName, @RequestBody VisitedLocation visitedLocation) {
        userClient.addUserLocation(userName, visitedLocation);
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody CreateUser createUser) {
        userClient.addUser(createUser);
    }

    @GetMapping("/getUserLocation")
    public Location getUserLocation(@RequestParam String userName) {
        return userClient.getUserLocation(userName);
    }

    @GetMapping("/getUserRewardsPoints")
    public int getUserRewardsPoints(@RequestParam String userName) {
        return userClient.getCumulativePointsUserRewards(userName);
    }

    @GetMapping("/getTripPricerTask")
    public TripPricerTask getTripPricerTask(@RequestParam String userName) {
        return userClient.getTripPricerTask(userName);
    }


}