package tourGuide.clients.dto.trackerservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Attraction extends Location {
    public final String attractionName;
    public final String city;
    public final String state;
    public final UUID attractionId;

    @JsonCreator
    public Attraction(@JsonProperty("attractionName")String attractionName,
                      @JsonProperty("city")String city,
                      @JsonProperty("state")String state,
                      @JsonProperty("latitude")double latitude,
                      @JsonProperty("longitude")double longitude) {
        super(latitude, longitude);
        this.attractionName = attractionName;
        this.city = city;
        this.state = state;
        this.attractionId = UUID.randomUUID();
    }
}
