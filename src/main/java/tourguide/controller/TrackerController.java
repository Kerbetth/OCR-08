package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.UserClient;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.FiveNearestAttractions;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.service.UserService;


import java.util.List;
import java.util.Map;

@RestController
public class TrackerController {

    @Autowired
    TrackerClient trackerClient;
    @Autowired
    PricerClient pricerClient;
    @Autowired
    UserService userService;

    /**
     * @param userName is the name of the user send in order to get the five neareest attraction around him
     * @return an object FiveNearestAttraction wich contain the required information
     */

    @GetMapping("/getNearestAttractions")
    public FiveNearestAttractions getNearestAttractions(@RequestParam String userName) {
        FiveNearestAttractions fiveNearestAttractions = trackerClient.get5NearestAttraction(userService.getCurrentLocation(userName));
        fiveNearestAttractions.setAttractionRewardPoints(pricerClient.getAttractionRewardsPoint(fiveNearestAttractions.getAttractionName()));
        return fiveNearestAttractions;
    }

    /**
     * @return the last location of all users. User id as the key, and a Location object as Value {longitude, latitude}
     */

    @GetMapping("/getAllCurrentUserLocations")
    public Map<String, Location> getAllCurrentUserLocations() {
        return userService.getLastLocationOfUsers();
    }

    /**
     * @return the current location of the user
     */
    @GetMapping("/trackUserLocation")
    public void trackUserLocation(@RequestParam String userId) {
        TrackerResponse trackerResponse = trackerClient.trackUserLocation(userId);
        if (trackerResponse != null) {
            userService.addUserLocation(userId, trackerResponse.visitedLocation);
            if (trackerResponse.attraction != null) {
                List<String> ids = userService.getVisitedAttractionIds(userId);
                for (String id : ids) {
                    if (trackerResponse.attraction.attractionId.toString().equals(id)) {
                        break;
                    } else userService.addUserReward(userId, pricerClient.generateUserReward(trackerResponse));
                }
            }

        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "userId does'nt exist in the database"
        );
    }
}