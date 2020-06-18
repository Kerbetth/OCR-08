package tourGuide.service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.domain.FiveNearestAttractions;
import tourGuide.clients.dto.UserService.User;
import tourGuide.clients.dto.UserService.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;

    public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    public void calculateRewards(User user) {
        CopyOnWriteArrayList<Attraction> attractions = new CopyOnWriteArrayList<>() ;
        CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>();
        attractions.addAll(gpsUtil.getAttractions());
        userLocations.addAll(user.getVisitedLocations());

        userLocations
                .stream()
                .forEach(visitedLocation -> attractions
                        .stream()
                        .filter(attraction -> nearAttraction(visitedLocation.location, attraction))
                        .forEach(attraction -> {
                            if (user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                                user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                            }
                        }));
/*
        for(VisitedLocation visitedLocation : userLocations) {
            for(Attraction attraction : attractions) {
                if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                    System.out.println("test");
                    if(nearAttraction(visitedLocation.location, attraction)) {
                        System.out.println("good");
                        user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                    }
                }
            }
        }*/
    }

    public FiveNearestAttractions get5nearestAttraction(VisitedLocation visitedLocation) {
        Map<Double, Attraction> attractionsByDistance = new TreeMap<>();
        FiveNearestAttractions fiveNearestAttractions = new FiveNearestAttractions();
        List<String> attractionsName = new ArrayList<>();
        List<Location> attractionsLocation = new ArrayList<>();
        List<Double> attractionsDistance = new ArrayList<>();
        List<Integer> attractionsRewardPoints = new ArrayList<>();
        int gatheredReward = 0;
        for (Attraction attraction : gpsUtil.getAttractions()) {
            attractionsByDistance.put(getDistance(attraction, visitedLocation.location), attraction);
        }

        attractionsByDistance.forEach((distance, attraction) -> {
            if (attractionsName.size() < 5) {
                attractionsName.add(attraction.attractionName);
                attractionsLocation.add(new Location(attraction.longitude, attraction.latitude));
                attractionsDistance.add(getDistance(attraction, visitedLocation.location));
                attractionsRewardPoints.add(rewardsCentral.getAttractionRewardPoints(attraction.attractionId, visitedLocation.userId));
            }
        });
        fiveNearestAttractions.setAttractionName(attractionsName);
        fiveNearestAttractions.setLatLongUser(visitedLocation.location);
        fiveNearestAttractions.setLatLongAttraction(attractionsLocation);
        fiveNearestAttractions.setDistance(attractionsDistance);
        for (Integer rewardPoints : attractionsRewardPoints) {
            gatheredReward += rewardPoints;
        }
        fiveNearestAttractions.setAttractionRewardPoints(gatheredReward);
        return fiveNearestAttractions;
    }

    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
    }

    private boolean nearAttraction(Location visitedLocation, Attraction attraction) {
        if(Math.abs(attraction.longitude-visitedLocation.longitude)< proximityBuffer){
            if(Math.abs(attraction.latitude-visitedLocation.latitude)< proximityBuffer){
                return getDistance(attraction, visitedLocation) > proximityBuffer ? false : true;
            }
        }
        return false;
    }

    private int getRewardPoints(Attraction attraction, User user) {
        //rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
        return ThreadLocalRandom.current().nextInt(1, 1000);
    }

    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }


}