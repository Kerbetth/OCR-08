package tourguide.integration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tourguide.clients.dto.CreateUser;
import tourguide.clients.dto.pricerreward.TripPricerTask;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.controller.UserController;
import tourguide.service.UserService;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UserControllerIT {

    @Autowired
    UserController userController;
    @Autowired
    UserService userService;
    @Test
    public void addUser() {
        userController.addUser(new CreateUser("jon", "111","email"));
        assertThat(userService.findUserByName("jon").getUserName()).isEqualTo("jon");
    }
/*
    @Test
    public void addUserLocation() {
        Location location = new Location(33.817595D, -117.922008D);
        userController.addUserLocation("internalUser1", new VisitedLocation(UUID.randomUUID(), location, new Date()));
        assertThat(userController.getUserLocation("internalUser1")).isEqualTo(location);
    }*/

    @Test
    public void getTripPricerTask() {
        assertThat(userController.getTripPricerTask("internalUser1")).isInstanceOf(TripPricerTask.class);
    }
}
