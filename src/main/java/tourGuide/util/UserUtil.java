package tourguide.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;


@Slf4j
@Repository
public class UserUtil {

    private Map<UUID, User> internalUserMap = new HashMap<>();

    private UserUtil(@Value("${testUserNumber}") int internalUserNumber, @Value("${testMode}") boolean testMode) {
        if (testMode) {
            IntStream.range(0, internalUserNumber).forEach(i -> {
                String userName = "internalUser" + i;
                String phone = "000";
                String email = userName + "@tourGuide.com";
                User user = new User(UUID.randomUUID(), userName, phone, email);
                user = generateUserLocationHistory(user);
                internalUserMap.put(user.getUserId(), user);
            });
            log.debug("Created " + internalUserNumber + " internal test users.");
        }
    }

    public void changeNumberOfUsers(int number) {
        internalUserMap.clear();
        IntStream.range(0, number).forEach(i -> {
            String userName = "internalUser" + i;
            String phone = "000";
            String email = userName + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), userName, phone, email);
            user = generateUserLocationHistory(user);
            internalUserMap.put(user.getUserId(), user);
        });

        log.debug("Created " + number + " internal test users.");
    }

    public Map<UUID, User> getInternalUserMap() {
        return internalUserMap;
    }
    public void setInternalUserMap(Map<UUID, User> internalUserMap) {
        this.internalUserMap = internalUserMap;
    }

    private User generateUserLocationHistory(User user) {
        List<VisitedLocation> visitedLocations = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> {
            visitedLocations.add(
                    new VisitedLocation(user.getUserId(),
                            new Location(generateRandomLatitude(),
                                    generateRandomLongitude()),
                            getRandomTime()));
        });
        visitedLocations.add(new VisitedLocation(user.getUserId(),
                new Location(33.817595D, -117.922008D),
                getRandomTime()));
        user.setVisitedLocations(visitedLocations);
        return user;
    }

    private double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }


}
