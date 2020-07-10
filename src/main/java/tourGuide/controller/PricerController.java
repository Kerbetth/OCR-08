package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.clients.PricerClient;
import tourGuide.clients.TrackerClient;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.pricerreward.Provider;
import tourGuide.clients.dto.pricerreward.TripPricerTask;

import java.util.List;

@RestController
public class PricerController {

    @Autowired
    PricerClient pricerClient;
    @Autowired
    TrackerClient trackerClient;
    @Autowired
    UserClient userClient;

    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        TripPricerTask tripPricerTask = userClient.getTripPricerTask(userName);
        int rewards = userClient.getCumulativePointsUserRewards(userName);
        return pricerClient.getTripDeals(tripPricerTask, rewards);
    }
}