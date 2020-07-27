package tourguide.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.SetUserPreferences;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.UserReward;
import tourguide.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIT {

    @Autowired
    UserService userService;


    String userId;

    @BeforeEach
    void setup() {
        userId = userService.getAllUsersID().get(0);
    }

    @Test
    public void setUserPreferences() {
        SetUserPreferences userPreferences = new SetUserPreferences(
                20, "USD", 0, 1000, 2, 2, 2, 1
        );
        userService.setUserPreferences("internalUser1", userPreferences);
    }

    @Test
    public void addUser() {
        CreateUser createUser = new CreateUser("internalUser1b");
        userService.addUser(createUser);
        List<VisitedLocation> user = userService.getAllVisitedLocation(userService.findUserByName("internalUser1b").getUserId().toString());
        assertThat(user).isEmpty();
    }

    @Test
    public void addUserLocation() {
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(1.0, 2.0), new Date());
        userService.addUserLocation(userId, visitedLocation);
        VisitedLocation visitedLocation1 = userService.getAllVisitedLocation(userId).get(4);
        assertThat(visitedLocation1.location.latitude).isEqualTo(1.0);
        assertThat(visitedLocation1.location.longitude).isEqualTo(2.0);
    }

    @Test
    public void addUserReward() {
        Attraction attraction = new Attraction("attraction", "city", "state", 1.0, 2.0);
        UserReward userReward = new UserReward(
                new VisitedLocation(UUID.randomUUID(),
                        new Location(1.0, 2.0),
                        new Date()),
                attraction,
                5);
        userService.addUserReward(userId, userReward);
        String attractionId = userService.getAttractionIds(userService.getAllUsersID().get(0)).get(0);
        assertThat(attractionId).isEqualTo(attraction.attractionId.toString());
    }

    @Test
    public void getUserLocation() {

        Location location = userService.getCurrentLocation("internalUser1");
        assertThat(location.longitude).isBetween(-180D, 180D);
        assertThat(location.latitude).isBetween(-86D, 86D);
    }

    @Test
    public void getAllUsersID() {
        List<String> uuids = userService.getAllUsersID();
        assertThat(uuids).hasSize(100);
    }

    @Test
    public void getAllVisitedLocations() {
        List<String> uuids = userService.getAllUsersID();
        assertThat(uuids).hasSize(100);
    }

    @Test
    public void getTripPricerTask() {
        List<String> uuids = userService.getAllUsersID();
        assertThat(uuids).hasSize(100);
    }

    @Test
    public void getUserRewards() {
        List<String> userRewards = userService.getAttractionIds(userId.toString());
        assertThat(userRewards).hasSize(0);
    }

    @Test
    public void getCumulateRewardPoints() {
        int userRewards = userService.getCumulateRewardPoints(userId.toString());
        assertThat(userRewards).isEqualTo(0);
    }
}
