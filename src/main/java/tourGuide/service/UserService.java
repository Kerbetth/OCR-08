package tourguide.service;


import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.pricerservice.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.User;
import tourguide.clients.dto.userservice.UserPreferences;
import tourguide.clients.dto.userservice.UserReward;
import tourguide.util.UserUtil;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    UserUtil userUtil;


    public void setUserPreferences(String userName,
                                   SetUserPreferences preferences) {
        CurrencyUnit currencyUnit = Monetary.getCurrency(preferences.getCurrencyUnit());
        User user = findUserByName(userName);
        UserPreferences userPreferences = new UserPreferences(
                preferences.getAttractionProximity(),
                currencyUnit,
                Money.of(preferences.getLowerPricePoint(), currencyUnit),
                Money.of(preferences.getHighPricePoint(), currencyUnit),
                preferences.getTripDuration(),
                preferences.getTicketQuantity(),
                preferences.getNumberOfAdults(),
                preferences.getNumberOfChildren()
        );

        user.setUserPreferences(userPreferences);
        userUtil.getInternalUserMap().put(user.getUserId(), user);
    }

    public void addUser(CreateUser createUser) {
        if(createUser.getUserName() != null) {
            ArrayList<VisitedLocation> vistedlocations = new ArrayList<>();

            User user = new User(
                    UUID.randomUUID(),
                    createUser.getUserName(),
                    createUser.getPhoneNumber(),
                    createUser.getEmailAddress());
            vistedlocations.add(new VisitedLocation(user.getUserId(),new Location(1.0,1.0),new Date()));
            user.setVisitedLocations(vistedlocations);
            if (!userUtil.getInternalUserMap().containsKey(user.getUserName())) {
                userUtil.getInternalUserMap().put(user.getUserId(), user);
            } else throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "the userName"+ createUser.getUserName() +" is already in use");
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "The field userName needs to be specified");

    }

    public void addUserLocation(String uuid, VisitedLocation visitedLocation) {
        userUtil.getInternalUserMap().get(UUID.fromString(uuid))
                .getVisitedLocations()
                .add(new VisitedLocation(UUID.fromString(uuid), visitedLocation.location, visitedLocation.timeVisited));
        log.info(userUtil.getInternalUserMap().get(UUID.fromString(uuid)).getUserName());
    }

    public void addUserReward(String uuid, UserReward userReward) {
        User user = userUtil.getInternalUserMap().get(UUID.fromString(uuid));
        List<UserReward> userRewards = user.getUserRewards();
        userRewards.add(userReward);
        Map<UUID, User> userMap = userUtil.getInternalUserMap();
        userMap.put(user.getUserId(), user);
        userUtil.setInternalUserMap(userMap);
        log.info("UserId: "+uuid+" has visited a new attraction and get rewards");
    }

    public User findUserByName(String userName) {
        return userUtil.getInternalUserMap().values().stream().filter(user -> user.getUserName().equals(userName)).findFirst().get();
    }

    public Location getCurrentLocation(String userName) {
        User user = findUserByName(userName);
        return user.getVisitedLocations().get(
                user.getVisitedLocations().size() - 1).location;
    }

    public List<VisitedLocation> getAllVisitedLocation(String userId) {
        return userUtil.getInternalUserMap().get(UUID.fromString(userId)).getVisitedLocations();
    }

    public List<String> getVisitedAttractionIds(String uuid) {
        return userUtil.getInternalUserMap().get(UUID.fromString(uuid)).getUserRewards().stream().map(UserReward::getAttractionId)
                .collect(Collectors.toList());
    }

    public List<String> getAllUsersName() {
        List<String> userName = new ArrayList<>();
        List<User> users = new ArrayList<>(userUtil.getInternalUserMap().values());
        users.stream().forEach(user -> userName.add(user.getUserName()));
        return userName;
    }

    public List<String> getAllUsersID() {
        List<String> userId = new ArrayList<>();
        List<User> users = new ArrayList<>(userUtil.getInternalUserMap().values());
        for (User user : users) {
            userId.add(user.getUserId().toString());
        }
        System.out.println(userId);
        return userId;
    }

    public TripPricerTask getTripPricerTask(String userName) {
        UserPreferences userPreferences = findUserByName(userName).getUserPreferences();
        return new TripPricerTask(
                "randomAPIKey",
                userPreferences.getNumberOfAdults(),
                userPreferences.getNumberOfChildren(),
                userPreferences.getTripDuration());
    }

    public Integer getCumulateRewardPoints(String userName) {
        List<UserReward> userRewards = userUtil.getInternalUserMap().get(findUserByName(userName).getUserId()).getUserRewards();
        return userRewards.stream().mapToInt(i -> i.getRewardPoints()).sum();
    }


    public int getUserRewardSize(String userId) {
        return userUtil.getInternalUserMap().get(UUID.fromString(userId)).getUserRewards().size();
    }

    public Map<String, Location> getLastLocationOfUsers() {
        Map<String, Location> lastLocation = new HashMap<>();
        userUtil.getInternalUserMap().values()
                .stream().forEach(
                        user -> lastLocation.put(
                                user.getUserId().toString(),
                                user.getVisitedLocations().get(user.getVisitedLocations().size()-1).location));
        return lastLocation;
    }
}
