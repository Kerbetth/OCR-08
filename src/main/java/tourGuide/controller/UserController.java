package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tourguide.clients.PricerClient;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.pricerservice.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.service.UserService;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PricerClient pricerClient;

    /**
     * set the preferences of the user by POST with a SetUserPreferences body
     */
    @PostMapping("/setUserPreferences")
    public void setUserPreferences(@RequestParam String userId,
            @RequestBody SetUserPreferences userPreferences) {
        userService.setUserPreferences(userId, userPreferences);
    }

    /**
     * add a User With the the help of the dto object CreateUser
     */
    @PostMapping("/addUser")
    public void addUser(@RequestBody CreateUser createUser) {
        userService.addUser(createUser);
    }

    /**
     * additions all reward points of the user
     */
    @GetMapping("/getUserRewardsPoints")
    public int getCumulativePointsUserRewards(@RequestParam String userName) {
        return userService.getCumulateRewardPoints(userName);
    }

    /**
     * additions all reward points of the user
     */
    @GetMapping("/getUserRewardSize")
    public int getUserRewardSize(@RequestParam String userId) {
        return userService.getUserRewardSize(userId);
    }

    @GetMapping("/getTripPricerTask")
    public TripPricerTask getTripPricerTask(@RequestParam String userName) {
        return userService.getTripPricerTask(userName);
    }

    /**
     * @return the last location of all users. User id as the key, and a Location object as Value {longitude, latitude}
     */

    @GetMapping("/getAllLastTrackedUserLocations")
    public Map<String, Location> getAllLastTrackedUserLocations() {
        return userService.getLastLocationOfUsers();
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