package tourGuide;

import helper.InternalTestHelper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.FiveNearestAttractions;
import tourGuide.clients.dto.trackerservice.Location;
import tourGuide.controller.TrackerController;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackerControllerIT {


	@Autowired
	TrackerController trackerController;
	Attraction attraction;

	@BeforeEach
	void setup() {
		attraction= new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
		InternalTestHelper.setInternalUserNumber(100);
	}
/*
	@Test
	public void shouldReturnNewRewardWhenUserVisitedAttraction() {
		//ARRANGE
		User user = new User(UUID.randomUUID(), "jon");
		List<VisitedLocation> visitedLocations = user.getVisitedLocations();
		visitedLocations.add(new VisitedLocation(user.getUserId(), attraction, new Date()));
		user.setVisitedLocations(visitedLocations);

		//ACT
		trackerController.trackUserLocation(user.getUserName());
		//tourGuideService.tracker.stopTracking();

		//ASSERT
		assertTrue(user.getUserRewards().size() == 1);
	}*/
	

	@Test
	public void shouldReturnLocationOfAllUser() {
		Map<UUID, Location> usersLocations = trackerController.getAllCurrentUserLocations();
		assertThat(usersLocations).hasSize(100);
	}

	@Test
	public void shouldReturnFiveNearestAttraction() {
		FiveNearestAttractions fiveNearestAttractions = trackerController.getNearestAttractions("internalUser1");
		assertThat(fiveNearestAttractions.getAttractionName()).hasSize(5);
	}
}
