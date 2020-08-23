package tourguide.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.TrackerClient;
import tourguide.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIT {

    @MockBean
    private PricerClient pricerClient;
    @MockBean
    private TrackerClient trackerClient;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserService userService;

    @Test
    public void addUser() throws Exception {
        this.mockMvc.perform(post("/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userName\":\"newUser\"}")
        )
                .andExpect(status().isOk());

        assertThat(userService.findUserByName("newUser").getUserName()).isEqualTo("newUser");
    }

    @Test
    public void getUserRewardsPoints() throws Exception {
        this.mockMvc.perform(get("/getUserRewardsPoints")
                .param("userName", "internalUser1")
        )
                .andExpect(status().isOk());
    }


    @Test
    public void getUserRewardSize() throws Exception {

        this.mockMvc.perform(get("/getUserRewardSize")
                .param("userId", userService.findUserByName("internalUser1").getUserId().toString())
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getTripPricerTask() throws Exception {
        this.mockMvc.perform(get("/getTripPricerTask")
                .param("userName", "internalUser1")
        )
                .andExpect(status().isOk());
    }
}
