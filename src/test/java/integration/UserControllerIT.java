package integration;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.clients.dto.CreateUser;
import tourGuide.clients.dto.pricerreward.TripPricerTask;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.clients.dto.trackerservice.VisitedLocation;
import tourGuide.controller.UserController;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UserControllerIT {

    @Autowired
    UserController userController;

    @Test
    public void addUser() {
        userController.addUser(new CreateUser("jon", "111","email"));
        assertThat(userController.getUserLocation("jon")).isInstanceOf(Location.class);
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
