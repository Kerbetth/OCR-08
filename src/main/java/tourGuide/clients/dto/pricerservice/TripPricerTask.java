package tourguide.clients.dto.pricerservice;


import java.util.UUID;

public class TripPricerTask {
    private final UUID attractionId;
    private final String apiKey;
    private final int adults;
    private final int children;
    private final int nightsStay;

    public TripPricerTask(String apiKey, int adults, int children, int nightsStay) {
        this.apiKey = apiKey;
        this.attractionId = UUID.randomUUID();
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
