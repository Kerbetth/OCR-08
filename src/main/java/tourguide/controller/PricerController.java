package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.UserClient;
import tourguide.clients.dto.pricerreward.Provider;
import tourguide.clients.dto.pricerreward.TripPricerTask;

import java.util.List;

@RestController
public class PricerController {

    @Autowired
    PricerClient pricerClient;
    @Autowired
    UserClient userClient;

    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        TripPricerTask tripPricerTask = userClient.getTripPricerTask(userName);
        int rewards = userClient.getCumulativePointsUserRewards(userName);
        return pricerClient.getTripDeals(tripPricerTask, rewards);
    }
}