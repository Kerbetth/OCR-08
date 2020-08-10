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
import tourguide.clients.dto.userservice.User;
import tourguide.clients.dto.userservice.UserReward;
import tourguide.service.UserService;
import tourguide.util.UserUtil;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserServiceIT {

    @Autowired
    UserService userService;
    @Autowired
    UserUtil userUtil;
    UUID uuid;

    @BeforeEach
    void setup() {
        uuid = UUID.randomUUID();
        Map<UUID, User> userMap = new HashMap<>();
        userMap.put(uuid, new User(uuid, "userTest"));
        userUtil.setInternalUserMap(userMap);

    }

    @Test
    public void setUserPreferences() {
        SetUserPreferences userPreferences = new SetUserPreferences(
                20, "USD", 0, 1000, 2, 2, 2, 1
        );
        userService.setUserPreferences("userTest", userPreferences);
    }

    @Test
    public void addUser() {
        CreateUser createUser = new CreateUser("internalUser1b");
        userService.addUser(createUser);
        List<VisitedLocation> allVisitedLocation = userService.getAllVisitedLocation(userService.findUserByName("internalUser1b").getUserId().toString());
        assertThat(allVisitedLocation).isEmpty();
    }

    @Test
    public void addUserLocation() {
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(1.0, 2.0), new Date());
        userService.addUserLocation(uuid.toString(), visitedLocation);
        VisitedLocation visitedLocation1 = userService.getAllVisitedLocation(uuid.toString()).get(0);
        assertThat(visitedLocation1.location.latitude).isEqualTo(1.0);
        assertThat(visitedLocation1.location.longitude).isEqualTo(2.0);
    }

    @Test
    public void addUserRewardAndGetVisitedAttractionIds() {
        //Arrange
        Attraction attraction = new Attraction("attraction", "city", "state", 1.0, 2.0);
        UserReward userReward = new UserReward(
                new VisitedLocation(UUID.randomUUID(),
                        new Location(1.0, 2.0),
                        new Date()),
                attraction,
                5);
        userService.addUserReward(uuid.toString(), userReward);

        //Act
        String attractionId = userService.getVisitedAttractionIds(userService.getAllUsersID().get(0)).get(0);

        //Assert
        assertThat(attractionId).isEqualTo(attraction.attractionId.toString());
    }

    @Test
    public void getUserLocation() {
        userService.addUserLocation(uuid.toString(), new VisitedLocation(UUID.randomUUID(),
                new Location(1.0, 2.0),
                new Date()));
        Location location = userService.getCurrentLocation("userTest");
        assertThat(location.longitude).isBetween(-180D, 180D);
        assertThat(location.latitude).isBetween(-86D, 86D);
    }

    @Test
    public void getAllUsersID() {
        List<String> uuids = userService.getAllUsersID();
        assertThat(uuids).hasSize(1);
    }


    @Test
    public void getCumulateRewardPoints() {
        int userRewards = userService.getCumulateRewardPoints("userTest");
        assertThat(userRewards).isEqualTo(0);
    }
}
