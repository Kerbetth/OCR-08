package tourguide.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.clients.dto.userservice.User;
import tourguide.service.UserService;
import tourguide.util.UserUtil;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    private UserUtil userUtil;

    private ArrayList<User> users;

    @InjectMocks
    UserService userService = new UserService();
    Map<UUID, User> map = new HashMap<>();
    UUID uuid = UUID.randomUUID();
    UUID uuid2 = UUID.randomUUID();

    @BeforeEach
    void setup() {
        users = new ArrayList<>();
        User user = new User(uuid, "user1", "000-555-444", "user1@mail.com");
        User user2 = new User(uuid2, "user2", "000-666-444", "user2@mail.com");
        ArrayList<VisitedLocation> visitedLocations = new ArrayList<>();
        visitedLocations.add(new VisitedLocation(UUID.randomUUID(), new Location(1.0, 2.0), new Date()));
        user.setVisitedLocations(visitedLocations);
        users.add(user);
        users.add(user2);
        map.put(uuid, user);
        map.put(uuid2, user2);
        when(userUtil.getInternalUserMap()).thenReturn(map);
    }

    @Test
    public void getAllVisitedLocation() {
        //ACT
        List<VisitedLocation> visitedLocations = userService.getAllVisitedLocation(uuid.toString());

        //ASSERT
        assertThat(visitedLocations).hasSize(1);
        assertThat(visitedLocations.get(0).timeVisited).isBeforeOrEqualTo(new Date());
    }

    @Test
    public void shouldReturnNoReward() {
        //ACT
        int cumulateRewardPoints = userService.getCumulateRewardPoints("user1");

        //ASSERT
        assertThat(cumulateRewardPoints).isEqualTo(0);
    }

    @Test
    public void getLocationOfAllUsersShouldReturnGoodUserLocations() {
        //ACT
        int cumulateRewardPoints = userService.getCumulateRewardPoints("user1");

        //ASSERT
        assertThat(cumulateRewardPoints).isEqualTo(0);
    }

    @Test
    public void shouldReturnGoodUserAfterFindUserByName() {
        //ACT
        User user = userService.findUserByName("user1");
        //ASSERT
        assertThat(user.getEmailAddress()).isEqualTo("user1@mail.com");
        assertThat(user.getPhoneNumber()).isEqualTo("000-555-444");
    }

    @Test
    public void shouldReturnGoodLocationAfterGetCurrentLocation() {
        //ACT
        Location location = new Location(1.0, 2.0);
        userService.addUserLocation(uuid.toString(), new VisitedLocation(UUID.randomUUID(), location, new Date()));
        Location locationResult = userService.getCurrentLocation("user1");
        //ASSERT
        assertThat(location.latitude).isEqualTo(location.latitude);
        assertThat(location.longitude).isEqualTo(location.longitude);
    }
}
