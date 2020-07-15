package unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tourGuide.clients.PricerClient;
import tourGuide.clients.TrackerClient;
import tourGuide.clients.UserClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PricerClient.class)
public class PricerControllerTest {

    @MockBean
    private PricerClient pricerClient;
    @MockBean
    private TrackerClient trackerClient;
    @MockBean
    private UserClient userClient;

    @Autowired
    protected MockMvc mockMvc;


    @Test
    public void getTripDealsController() throws Exception {
        this.mockMvc.perform(get("/getTripDeals")
                .param("userName", "aName")
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getErrorWithTripDealsControllerIfNoParam() throws Exception {
        this.mockMvc.perform(get("/getTripDeals")
        )
                .andExpect(status().is4xxClientError());
    }

}
