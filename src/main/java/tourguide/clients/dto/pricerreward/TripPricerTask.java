package tourguide.clients.dto.pricerreward;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class TripPricerTask {
    private final UUID attractionId;
    private final String apiKey;
    private final int adults;
    private final int children;
    private final int nightsStay;

    @JsonCreator
    public TripPricerTask(
            @JsonProperty("apiKey")String apiKey,
            @JsonProperty("attractionId")UUID attractionId,
            @JsonProperty("adults")int adults,
            @JsonProperty("children")int children,
            @JsonProperty("nightsStay")int nightsStay) {
        this.apiKey = apiKey;
        this.attractionId = attractionId;
        this.adults = adults;
        this.children = children;
        this.nightsStay = nightsStay;
    }

    public UUID getAttractionId() {
        return attractionId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public int getAdults() {
        return adults;
    }

    public int getChildren() {
        return children;
    }

    public int getNightsStay() {
        return nightsStay;
    }
}
