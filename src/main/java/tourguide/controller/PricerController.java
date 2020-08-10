package tourguide.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourguide.clients.PricerClient;
import tourguide.clients.dto.pricerreward.Provider;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.service.UserService;

import java.util.List;

@RestController
public class PricerController {

    @Autowired
    PricerClient pricerClient;
    @Autowired
    UserService userService;

    /**
     *
     * @param userName is the name of the user send in order to get the tripdeals who fits with this user
     * @return a list of providers with the name and the offer price
     */

    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        TripPricerTask tripPricerTask = userService.getTripPricerTask(userName);
        int rewards = userService.getCumulateRewardPoints(userName);
        return pricerClient.getTripDeals(tripPricerTask, rewards);
    }
}