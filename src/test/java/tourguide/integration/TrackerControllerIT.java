package tourguide.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.*;
import tourguide.service.UserService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TrackerControllerIT {


	@MockBean
	private PricerClient pricerClient;
	@MockBean
	private TrackerClient trackerClient;

	@Autowired
	protected UserService userService;
	@Autowired
	protected MockMvc mockMvc;

	Attraction attraction;
	int userNumber;

	@BeforeEach
	void setup(@Value("${testUserNumber}") int internalUserNumber) {
		attraction= new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
		userNumber= internalUserNumber;
	}

	@Test
	public void shouldReturnFiveNearestAttraction() throws Exception {
		TrackerResponse trackerResponse =new TrackerResponse(
				new VisitedLocation(UUID.randomUUID(),
						new Location(1.0,1.0),
						new Date()),
				null);
		List<NearAttraction> nearAttractions = new ArrayList<>();
		nearAttractions.add(new NearAttraction());
		FiveNearestAttractions fiveNearestAttractions =new FiveNearestAttractions(new Location(2.0,2.0), nearAttractions);

		when(trackerClient.get5NearestAttraction(any())).thenReturn(fiveNearestAttractions);
		when(trackerClient.trackUserLocation(anyString())).thenReturn(trackerResponse);
		this.mockMvc.perform(get("/getNearestAttractions")
				.param("userName","internalUser1")
		)
				.andExpect(status().isOk());
	}

	@Test
	public void getAllCurrentLocationShouldReturnSameNumberAsUser() throws Exception {
		this.mockMvc.perform(get("/getAllCurrentUserLocations")
		)
				.andExpect(status().isOk());
	}

	@Test
	public void getTrackUserLocationWithWrongId() throws Exception {
		TrackerResponse trackerResponse =new TrackerResponse(
				new VisitedLocation(UUID.randomUUID(),
						new Location(1.0,1.0),
						new Date()),
				null);

		this.mockMvc.perform(get("/trackUserLocation?userId=aFalseId")
		)
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void getTrackUserLocation() throws Exception {
		TrackerResponse trackerResponse =new TrackerResponse(
				new VisitedLocation(UUID.randomUUID(),
						new Location(1.0,1.0),
						new Date()),
				null);
		when(trackerClient.trackUserLocation(anyString())).thenReturn(trackerResponse);
		this.mockMvc.perform(get("/trackUserLocation")
				.param("userId", userService.findUserByName("internalUser1").getUserId().toString())
		)
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnErrorIfGetTrackUserLocationWithWrongId() throws Exception {
		this.mockMvc.perform(get("/trackUserLocation")
				.param("userId", "aFalseId")
		)
				.andExpect(status().is4xxClientError());
	}

}
