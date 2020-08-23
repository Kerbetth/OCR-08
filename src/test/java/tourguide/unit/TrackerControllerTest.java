package tourguide.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.clients.UserClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class TrackerControllerTest {

    @MockBean
    private PricerClient pricerClient;
    @MockBean
    private TrackerClient trackerClient;
    @MockBean
    private UserClient userClient;


    @Autowired
    protected MockMvc mockMvc;


    @Test
    public void getTrackUserLocationWithWrongId() throws Exception {
        this.mockMvc.perform(get("/trackUserLocation")
                .param("userId", "anId")
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void getTrackUserLocationWithoutId() throws Exception {
        this.mockMvc.perform(get("/trackUserLocation")
        )
                .andExpect(status().is4xxClientError());
    }

}
