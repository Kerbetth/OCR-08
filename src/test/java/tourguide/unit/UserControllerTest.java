package tourguide.unit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.UserClient;
import tourguide.clients.dto.TrackerResponse;
import tourguide.clients.dto.trackerservice.Location;
import tourguide.clients.dto.trackerservice.VisitedLocation;
import tourguide.controller.UserController;
import tourguide.service.UserService;

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private PricerClient pricerClient;
    @MockBean
    private TrackerClient trackerClient;
    @MockBean
    private UserService userService;


    @Autowired
    protected MockMvc mockMvc;

    @Test

    public void getUserRewardsPoints() throws Exception {
        TrackerResponse trackerResponse =new TrackerResponse(
                new VisitedLocation(UUID.randomUUID(),
                        new Location(1.0,1.0),
                        new Date()),
                null);
        when(userService.getCumulateRewardPoints(anyString())).thenReturn(3);

        this.mockMvc.perform(get("/getUserRewardsPoints?userName=aFalseId")
        )
                .andExpect(status().isOk());
    }
}
