package tourguide.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.service.UserService;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackerClient.class)
public class TrackerControllerTest {

    @MockBean
    private PricerClient pricerClient;
    @MockBean
    private TrackerClient trackerClient;
    @MockBean
    private UserService userService;

    @Autowired
    protected MockMvc mockMvc;


    @Test

    public void getTrackUserLocation() throws Exception {
        TrackerResponse trackerResponse =new TrackerResponse(
                new VisitedLocation(UUID.randomUUID(),
                        new Location(1.0,1.0),
                        new Date()),
                null);
        when(trackerClient.trackUserLocation(anyString())).thenReturn(trackerResponse);

        this.mockMvc.perform(get("/trackUserLocation")
                .param("userId", "aFalseId")
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
    @Test
    public void getErrorWithTripDealsControllerIfNoParam() throws Exception {
        this.mockMvc.perform(get("/getTripDeals")
        )
                .andExpect(status().is4xxClientError());
    }

}
