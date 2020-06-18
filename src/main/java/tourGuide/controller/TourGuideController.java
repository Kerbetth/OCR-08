package tourGuide.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;


import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.UserLocation;
import tourGuide.domain.User;
import tourGuide.service.ClientService;

@RestController
public class TourGuideController {

    @Autowired
    ClientService clientService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    //  TODO: Change this method to no longer return a List of Attractions.
    //  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
    //  Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the attractions.
    // The reward points for visiting each Attraction.
    //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public FiveNearestAttractions getNearbyAttractions(@RequestParam String userName) {
        return clientService.get5NearestAttraction(userName);
    }

    @RequestMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        return JsonStream.serialize(userClient.getUserRewards(getUser(userName)));
    }

    @PostMapping("/setUserPreferences")
    public void setUserPreferences(
            @RequestParam String userName,
            Integer numberOfAdults,
            Integer numberOfChildren,
            Integer tripDuration,
            Integer highPricePoint,
            Integer lowerPricePoint
    ) {
        if (highPricePoint == 0) {
            highPricePoint = 999_999;
        }
        userClient.setUserPreferences(userName, numberOfAdults, numberOfChildren, tripDuration, highPricePoint, lowerPricePoint);
    }

    @RequestMapping("/getAllCurrentLocations")
    public Map<UUID, Location> getAllCurrentLocations() {
        // TODO: Get a list of every user's most recent location as JSON
        //- Note: does not use gpsUtil to query for their current location,
        //        but rather gathers the user's current location from their stored location history.
        //
        // Return object should be the just a JSON mapping of userId to Locations similar to:
        //     {
        //        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371}
        //        ...
        //     }
        return clientService.getAllCurrentLocations();
    }

    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = userClient.getTripDeals(getUser(userName));
        return providers;
    }

    private User getUser(String userName) {
        return userClient.getUser(userName);
    }
}