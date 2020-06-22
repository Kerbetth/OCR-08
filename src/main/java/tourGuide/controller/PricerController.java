package tourGuide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tourGuide.clients.PricerClient;
import tourGuide.clients.dto.pricerreward.Provider;

import java.util.List;

@RestController
public class PricerController {

    @Autowired
    PricerClient pricerClient;

    @GetMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = pricerClient.getTripDeals(userName);
        return providers;
    }

    @GetMapping("/getRewards")
    public Integer getRewards(@RequestParam String userName) {
        return pricerClient.getUserRewards(userName);
    }

}