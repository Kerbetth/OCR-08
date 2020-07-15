package tourGuide.clients.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tourGuide.clients.dto.trackerservice.Attraction;
import tourGuide.clients.dto.trackerservice.VisitedLocation;

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
