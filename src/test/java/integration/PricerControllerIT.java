package integration;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.clients.dto.pricerreward.Provider;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.controller.PricerController;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PricerControllerIT {


    @Autowired
	PricerController pricerController;
    Attraction attraction;

    @BeforeEach
    void setup() {
        attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
    }

    @Test
    public void getTripDeals() {
        List<Provider> providers = pricerController.getTripDeals("internalUser1");
        assertEquals(10, providers.size());
    }

}
