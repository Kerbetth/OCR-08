package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.clients.PricerClient;
import tourGuide.clients.TrackerClient;
import tourGuide.clients.UserClient;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.UserLocation;
import tourGuide.domain.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    UserClient userClient;
    @Autowired
    PricerClient pricerClient;
    @Autowired
    TrackerClient trackerClient;


    public FiveNearestAttractions get5NearestAttraction(String userName) {
        Location location = userClient.getUser(userName).getLastVisitedLocation().location;
        return trackerClient.get5NearestAttraction(location);
    }

    public Map<UUID, Location> getAllCurrentLocations() {
        return userClient.getAllCurrentLocations();
    }


}
