package tourguide.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tourguide.clients.PricerClient;
import tourguide.clients.dto.pricerreward.Provider;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.controller.PricerController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PricerControllerIT {

    @MockBean
    private PricerClient pricerClient;

    @Autowired
    protected MockMvc mockMvc;

    Attraction attraction;

    @BeforeEach
    void setup() {
        attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
    }


    @Test
    public void getTripDealsController() throws Exception {
        List<Provider> providers = new ArrayList<>();
        providers.add(new Provider(UUID.randomUUID(), "provider1", 1));
        when(pricerClient.getTripDeals(any(), anyInt())).thenReturn(providers);
        String test = this.mockMvc.perform(get("/getTripDeals")
                .param("userName", "internalUser1")
        )
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(
                test,
                objectMapper.writeValueAsString(providers));
    }
}
