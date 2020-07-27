package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;

import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.FiveNearestAttractions;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.service.UserService;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TrackerController {

    @Autowired
    TrackerClient trackerClient;
    @Autowired
    PricerClient pricerClient;
    @Autowired
    UserService userService;

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @GetMapping("/getNearestAttractions")
    public FiveNearestAttractions getNearestAttractions(@RequestParam String userName) {
        FiveNearestAttractions fiveNearestAttractions = trackerClient.get5NearestAttraction(userService.getCurrentLocation(userName));
        fiveNearestAttractions.setAttractionRewardPoints(pricerClient.getAttractionRewardsPoint(fiveNearestAttractions.getAttractionName()));
        return fiveNearestAttractions;
    }

    @GetMapping("/getAllCurrentUserLocations")
    public Map<String, Location> getAllCurrentUserLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }
        return trackerClient.getCurrentLocationOfAllUsers(userService.getAllUsersID());
    }

    @GetMapping("/trackUserLocation")
    public void trackUserLocation(@RequestParam String userId) {
        TrackerResponse trackerResponse = trackerClient.trackUserLocation(userId);
        if(trackerResponse!=null) {
            userService.addUserLocation(userId, trackerResponse.visitedLocation);
            if (trackerResponse.attraction != null) {
                List<String> ids = userService.getAttractionIds(userId);
                for (String id : ids) {
                    if (trackerResponse.attraction.attractionId.toString().equals(id)) {
                        break;
                    } else userService.addUserReward(userId, pricerClient.generateUserReward(trackerResponse));
                }
            }

        }
        else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "userId does'nt exist in the database"
        );
    }
}