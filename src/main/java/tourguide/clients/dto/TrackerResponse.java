package tourguide.clients.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tourguide.clients.dto.trackerservice.Attraction;
import tourguide.clients.dto.trackerservice.VisitedLocation;

public class TrackerResponse {
    public final VisitedLocation visitedLocation;
    public final Attraction attraction;

    @JsonCreator
    public TrackerResponse(@JsonProperty("visitedLocation")VisitedLocation visitedLocation,
                           @JsonProperty("attraction")Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }
}
